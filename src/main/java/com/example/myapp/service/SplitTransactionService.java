package com.example.myapp.service;

import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import com.example.myapp.dto.*;
import com.example.myapp.exception.InvalidDataException;
import com.example.myapp.model.*;
import com.example.myapp.repository.*;
import com.example.myapp.utils.ProjectUtils;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SplitTransactionService {
    @Autowired
    SplitTransactionRepository splitTransactionRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    ProjectUtils projectUtils;

    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    ScopeRepository scopeRepository;

    private static final PrettyLogger logger = PrettyLogger.getLogger(SplitTransactionService.class);

    @Transactional
    public List<SplitTransaction> splitTransactions(CreateSplitRequest createSplitRequest) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        User user = projectUtils.getUserFromToken();
        Long transactionId = createSplitRequest.transactionId();
        Transaction transaction = null;
        if(transactionId != null) {
            logger.info("fetching transaction from db");
            transaction = transactionRepository.getTransactionById(user.getId(), transactionId);
        }

        List<Friends> allFriends = friendRepository.findAllFriendsByUserId(user.getId());

        if(transaction != null) {
            validateSplitedAmount(transaction, createSplitRequest);
            updateTransaction(transaction, ctx, createSplitRequest);
        }

        checkAllFriendsExistOrNot(allFriends, createSplitRequest);

        List<SplitTransaction> splitTransactions = new ArrayList<>();

        Friends payToFriend = getFriendFromList(allFriends, createSplitRequest.payToFriendId());

        for(int i = 0; i < createSplitRequest.shares().size(); i++){
            SplitTransaction splitTransaction = new SplitTransaction();

            if(transaction != null) {
                splitTransaction.setTransaction(transaction);
            }

            splitTransaction.setUser(user);

            splitTransaction.setAmount(createSplitRequest.shares().get(i).amount());
            splitTransaction.setIsSettled(createSplitRequest.shares().get(i).isSettled());
            splitTransaction.setPayToFriend(payToFriend);

            Friends friend = getFriendFromList(allFriends, createSplitRequest.shares().get(i).paidByFriendId());

            splitTransaction.setPaidByFriend(friend);

            splitTransactions.add(splitTransaction);
        }

        return splitTransactionRepository.saveAll(splitTransactions);

    }

    private void updateTransaction(Transaction transaction, GenericRequestContext ctx, CreateSplitRequest createSplitRequest) {

        if(transaction.getIsSplitted()){
            throw new InvalidDataException("This transaction is already splitted");
        }

        Double originalAmount = transaction.getAmount();

        transaction.setIsSplitted(Boolean.TRUE);
        Double settledAmount = calculateSettleAmount(createSplitRequest);
        Double unsettledAmount = transaction.getAmount() - settledAmount;
        Double myAmount = getMyAmountFromRequest(createSplitRequest);

        transaction.setSettledAmount(settledAmount);
        transaction.setUnSettledAmount(unsettledAmount);
        if(myAmount != null){
            transaction.setMyAmount(myAmount);
        }

        if(unsettledAmount.equals(0.0)){
            transaction.setIsSettled(true);
        } else {
            transaction.setIsSettled(false);
        }

        updateScopeAndBudget(transaction, originalAmount);

        transactionRepository.save(transaction);
    }

    private void updateScopeAndBudget(Transaction transaction, Double originalAmount) {
        Scope scope = transaction.getScope();

        User user = transaction.getUser();

        LocalDateTime txnDate = transaction.getTxnDate();

        int budgetMonth = txnDate.getMonthValue();
        int budgetYear = txnDate.getYear();

        Budget budget = budgetRepository.getBudgetByMonthYearScopeAndUser(budgetMonth, budgetYear, scope.getId(), user.getId());

        logger.info("Reverting the budget and scope with original transaction amount");

        budget.setUsedBudget(budget.getUsedBudget() - originalAmount);
        budget.setRemainingBudget(budget.getRemainingBudget() + originalAmount);

        scope.setOverAllRemainingBudget(scope.getOverAllRemainingBudget() + originalAmount);
        scope.setOverAllUsedBudget(scope.getOverAllUsedBudget() - originalAmount);

        logger.info("Updating the budget and scope again with split data");
        budget.setUsedBudget(budget.getUsedBudget() + transaction.getMyAmount());
        budget.setRemainingBudget(budget.getRemainingBudget() - transaction.getMyAmount());

        scope.setOverAllRemainingBudget(scope.getOverAllRemainingBudget() - transaction.getMyAmount());
        scope.setOverAllUsedBudget(scope.getOverAllUsedBudget() + transaction.getMyAmount());

        budgetRepository.save(budget);
        scopeRepository.save(scope);
    }

    private Double getMyAmountFromRequest(CreateSplitRequest createSplitRequest) {
        Long payToFriend = createSplitRequest.payToFriendId();

        for(int i = 0; i < createSplitRequest.shares().size(); i++){

            if(payToFriend.equals(createSplitRequest.shares().get(i).paidByFriendId())){
                return createSplitRequest.shares().get(i).amount();
            }
        }

        return 0.0;
    }

    private Double calculateSettleAmount(CreateSplitRequest createSplitRequest) {
        Double settledAmount = 0.0;

        for(int i = 0; i < createSplitRequest.shares().size(); i++){

            if(createSplitRequest.shares().get(i).isSettled()){
                settledAmount += createSplitRequest.shares().get(i).amount();
            }
        }

        return settledAmount;
    }

    private Double calculateSettleAmount(List<SplitTransaction> splitTransactionList) {
        Double settledAmount = 0.0;

        for(int i = 0; i < splitTransactionList.size(); i++){

            if(splitTransactionList.get(i).getIsSettled()){
                settledAmount += splitTransactionList.get(i).getAmount();
            }
        }

        return settledAmount;
    }

    private void validateSplitedAmount(Transaction transaction, CreateSplitRequest createSplitRequest) {
        Double amount = 0.0;

        for(int i = 0; i < createSplitRequest.shares().size(); i++){
            amount += createSplitRequest.shares().get(i).amount();
        }

        if(!amount.equals(transaction.getAmount())){
            throw new InvalidDataException("Splited amount is not equal to transaction amount");
        }
    }

    private Friends getFriendFromList(List<Friends> allFriends, @NotNull(message = "PaidByFriendId is required") Long friendId) {
        for(Friends tempFriend: allFriends){
            if(tempFriend.getId().equals(friendId)){
                return tempFriend;
            }
        }

        return null;
    }

    private void checkAllFriendsExistOrNot(List<Friends> allFriends, CreateSplitRequest createSplitRequest) {
        List<Long> friendsId = new ArrayList<>();

        for(Friends tempFriend: allFriends){
            friendsId.add(tempFriend.getId());
        }

        boolean isAllFriendsPresent = friendsId.contains(createSplitRequest.payToFriendId());

        for(int i = 0; i < createSplitRequest.shares().size(); i++){
            if(!friendsId.contains(createSplitRequest.shares().get(i).paidByFriendId())){
                isAllFriendsPresent = false;
                break;
            }
        }

        if(!isAllFriendsPresent){
            throw new InvalidDataException("All the friends are not present in the database");
        }
    }

    @Transactional
    public List<SplitResponse> getAllSplits(){
        User user = projectUtils.getUserFromToken();
        if (user == null) {
            return List.of(); // Or handle as an error
        }

        List<SplitTransaction> splitResponseList = splitTransactionRepository.findAllSplitByUserId(user.getId());

        return splitResponseList.stream()
                .map(spliteTransaction -> new SplitResponse(
                        spliteTransaction.getId(),
                        spliteTransaction.getPayToFriend().getId(),
                        spliteTransaction.getPaidByFriend().getId(),
                        spliteTransaction.getTransaction().getId(),
                        spliteTransaction.getAmount(),
                        spliteTransaction.getIsSettled()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<SplitResponse> getAllSplitsWithTxnId(Long txnId) {
        User user = projectUtils.getUserFromToken();
        if (user == null) {
            return List.of(); // Or handle as an error
        }

        List<SplitTransaction> splitResponseList = splitTransactionRepository.findAllSplitByUserIdAndTxnId(user.getId(), txnId);

        return splitResponseList.stream()
                .map(spliteTransaction -> new SplitResponse(
                        spliteTransaction.getId(),
                        spliteTransaction.getPayToFriend().getId(),
                        spliteTransaction.getPaidByFriend().getId(),
                        spliteTransaction.getTransaction().getId(),
                        spliteTransaction.getAmount(),
                        spliteTransaction.getIsSettled()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<SplitResponse> updateSplitStatus(UpdateSplitStatusRequest updateSplitStatusRequest) {
        User user = projectUtils.getUserFromToken();
        if (user == null) {
            throw new InvalidDataException("User not found in context.");
        }

        logger.info("sdkljf askdjf;alsdjf;aksd" + updateSplitStatusRequest.toString());
        // Fetch the associated transaction
        Transaction transaction = transactionRepository.getTransactionById(user.getId(), updateSplitStatusRequest.txnId() );
        if (transaction == null) {
            throw new InvalidDataException("Transaction not found.");
        }

        // Fetch all splits for this transaction and user
        List<SplitTransaction> splitTransactionList =
                splitTransactionRepository.findAllSplitByUserIdAndTxnId(user.getId(), transaction.getId());

        // Map updates for quick access
        Map<Long, Boolean> updateMap = updateSplitStatusRequest.updates().stream()
                .collect(Collectors.toMap(UpdateSplitStatusRequest.SplitStatusUpdate::splitId,
                        UpdateSplitStatusRequest.SplitStatusUpdate::isSettled));

        List<SplitResponse> updatedResponses = new ArrayList<>();

        for (SplitTransaction split : splitTransactionList) {
            Boolean newStatus = updateMap.get(split.getId());

            split.setIsSettled(newStatus);
        }

        for(SplitTransaction s: splitTransactionList) {
            logger.info("asdlkfj ;sadkjfkasdj lfkasdjflk " + s.toString());
        }
        // Save updated splits in bulk
        splitTransactionRepository.saveAll(splitTransactionList);

        Double settledAmount = calculateSettleAmount(splitTransactionList);
        Double unsettledAmount = transaction.getAmount() - settledAmount;

        transaction.setSettledAmount(settledAmount);
        transaction.setUnSettledAmount(unsettledAmount);

        if(unsettledAmount.equals(0.0)){
            transaction.setIsSettled(true);
        } else {
            transaction.setIsSettled(false);
        }

        transactionRepository.save(transaction);

        return updatedResponses;
    }


}
