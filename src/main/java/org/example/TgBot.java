package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButtonPollType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;


// TODOS LIST:
// TODO: Handle Users answers
// TODO: IF Users answered or Passed 5 min close the [Survey]
// TODO: CalculatePresent only one func but we can pass as a lambda
public class TgBot extends TelegramLongPollingBot {
   // private final List<InlineKeyboardButton> amount_questions_buttons = new ArrayList<>();
   // private final List<InlineKeyboardButton> amount_answers_buttons = new ArrayList<>();
    private final Map<Long, MyUser> users_map = new HashMap<>();
    private Survey currentSurvey = null;
    private final Map<Long, SurveyTracker> surveyTrackers = new HashMap<>();
    private String question;
    private final List<String> options = new ArrayList<>();
    private boolean wantCreateSurvey;
    private boolean surveyIsCompleted;
    private SurveyAnswers answers;
    private Time time;


    @Override
    public synchronized void onUpdateReceived(Update update) {
        if(update.hasMessage()) {
            handleMessage(update);
        } else if (update.hasCallbackQuery() && users_map.size() > 0) {
             handleButtonClick(update);
        }
    }

    private void handleMessage(Update update) {
        Long chatId = update.getMessage().getChatId();
        String userName = update.getMessage().getFrom().getFirstName();
        String messageText = update.getMessage().getText();
        MyUser user = new MyUser(chatId, userName);

       // user.setLastMessage(messageText);

        if (!users_map.containsKey(chatId)) {
            if (messageText.equals("/start") || messageText.equals("Hi")) {
                checkUserJoined(chatId, user);

            }else {
                sendMessage(chatId, "Click /start or write Hi.");
            }
        }
        // TODO: change it
        else {

            if(messageText.equals("/createsurvey") || wantCreateSurvey) {
                wantCreateSurvey = true;
                if(!SurveyTracker.isSurveyIsStarted()) {
                    createSurvey(chatId, user, messageText);
                }else {
                    assert currentSurvey != null;
                    // TODO: implement the logic for getting the answers from users
                    llllllllllllllllllllllll(chatId, messageText);

                }
                ////////////////////////////////////////////////////////

            }else if (messageText.equals("/start") || messageText.equals("Hi")) {
                sendMessage(chatId, "You already start the bot!");
                sendMessage(chatId, "to create a survey /createsurvey");
            }else {
                sendMessage(chatId, "See /menu how to deal with the bot ");
            }
        }

    }

    private void llllllllllllllllllllllll(Long chatId, String messageText) {
        if(currentSurvey.getCreatorName().equals(users_map.get(chatId).getFirstName())) {

            if(answers == null) {
                answers = new SurveyAnswers(currentSurvey);
                answers.setHandleQuestionNumber(true);
                // Start the survey with 5 minuts
                time = new Time();
                long startTime = System.currentTimeMillis();

                Thread t = new Thread(() -> {
                    while(!surveyIsCompleted) {
                        System.out.print("Time passed: ");
                        System.out.println(System.currentTimeMillis() - startTime);
                    }
                });
                t.start();
            }
            answers.setAnsweringUser(users_map.get(chatId));

            if(!answers.isUserAnsweredOnAllQuestion()) {
                handleInputFromUserForAnswers(chatId, answers, messageText);
            }else {
                sendMessage(chatId, "Wait until time passed or others will answer on all questions");
            }

            int amountAnsweredUsers = amountOfAnsweredUsers();
            // TODO: End of Survey

            if (time.isFiveMinutesOver() || amountAnsweredUsers >= users_map.size()) {
                surveyIsCompleted = true;
                sendMessageToAllUsers("The Survey has been closed! ");
                sendMessage(currentSurvey.getCreatorChatId(), "Sending the results of survey");

                // TODO: Total answered users
                double present = calculatePresent(amountAnsweredUsers, users_map.size());
                sendMessage(currentSurvey.getCreatorChatId(), "The present is " + present);

            }

            if(surveyIsCompleted) {
                surveyIsCompleted = false;
                SurveyTracker.setSurveyStart(false);
                time.resetTimer();
            }

        }else {
            sendMessage(chatId, "You are the creator you can not answer! wait to others ");
        }
    }

    private double calculatePresent(int answeredUsers, int totalUsers) {
        return ((double) answeredUsers / (double)totalUsers) * 100;
    }



    private int amountOfAnsweredUsers() {
        int count = 0;
        for(MyUser user : users_map.values()) {
            if(user.isAnswered()) {
                count++;
            }
        }
        return count;
    }

    // TODO: implement
    private void handleInputFromUserForAnswers(Long chatId, SurveyAnswers answers, String messageText) {
        if(answers.isHandleQuestionNumber()) {
            getQuestionNumber(chatId, answers, messageText);
        }else if(answers.isHandleAnswerNumber()) {
            getAnswerNumber(chatId, answers, messageText);
        }else {
            throw new Error("Unreachable placement ");
        }
    }

    private void getQuestionNumber(Long chatId, SurveyAnswers tracker, String messageText) {
        try {
            int questionId = Integer.parseInt(messageText);
            if (questionId >= 1 && questionId <= tracker.max_question_count()) {
                // TODO: here
                if(!tracker.isAlreadyAnswered(questionId)) {
                    tracker.setQuestionId(String.valueOf(questionId));
                    sendMessage(chatId, "Choose answer for question " + questionId);
                    tracker.setHandleQuestionNumber(false);
                    tracker.setHandleAnswerNumber(true);
                }else { sendMessage(chatId, "You already answered this question choose another !");}

            } else { sendMessage(chatId, "Enter a valid index of question"); }

        }catch (NumberFormatException e) {
            sendMessage(chatId, "Please enter valid number!");
        }
    }

    // TODO: introduce a map that maps question number to answer
    private void getAnswerNumber(Long chatId, SurveyAnswers tracker, String messageText) {
        try {
            int answerCount = Integer.parseInt(messageText);
            if(answerCount >= 1 && answerCount <= tracker.max_answers_for_spec_question()) {

                users_map.get(chatId).setAnswered(true);
                tracker.mapAnswerNumberForQuestion(String.valueOf(answerCount));

                tracker.setHandleQuestionNumber(false);

                tracker.setHandleAnswerNumber(true);
            }else {
                sendMessage(chatId, "Enter a valid number of answers! Choose(2-4)");
            }
        }catch (NumberFormatException e) {
            sendMessage(chatId, "Please enter a valid number!");
        }
    }

    private void checkUserJoined(Long chatId, MyUser user) {
        if(!user.isJoined()) {
            user.setJoined(true);
            users_map.put(chatId, user);
            sendMessageToAllUsers("User " + user.getFirstName() + " has joined to our community! we have already "  + users_map.size() + " members.");
        }else {
            sendMessage(chatId, "You already part of the community");
        }
    }
//   /*The main problem how to don't gave to the user click the same button twice*/
//
//   TODO: handle button click
    private void handleButtonClick(Update update) {
        String buttonData = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        // String userName = update.getCallbackQuery().getFrom().getFirstName();
        MyUser user = users_map.get(chatId);
        //user.setLastButtonClicked(buttonData);

        SurveyTracker tracker = surveyTrackers.get(chatId);

        if(SurveyTracker.isSurveyIsStarted() && currentSurvey != null) {
            return;
        }


        int numOfQuestions = 0;
        int numOfAnswers = 0;
        switch (buttonData) {
            case "qBtn1": // handel question buttons
                numOfQuestions = 1; break;
            case "qBtn2":
                numOfQuestions = 2; break;
            case "qBtn3":
                numOfQuestions = 3; break;
            case "aBtn1": // handling answers buttons
                numOfAnswers = 2; break;
            case "aBtn2":
                numOfAnswers = 3; break;
            case "aBtn3":
                numOfAnswers = 4; break;
            case "start1":
                startSurvey(chatId, tracker);
                SurveyTracker.setSurveyStart(true); break;
            case "timeStart":
                sendMessage(chatId, "How much second hold the Quizz? "); break;
        }

//        if(numOfQuestions > 0 && user.getCountQuestionButtonsClicked() < 3) {
//           // tracker.setNumberOfQuestions(numOfQuestions);
//        }
//
//        if(numOfAnswers > 1 && user.getCountAnswersButtonsClicked() < 3) {
//            //tracker.setAnswerCountForCurrentQuestion(numOfAnswers);
//        }

    }

    private void createSurvey(Long chatId, MyUser user, String message) {
        SurveyTracker tracker = surveyTrackers.get(chatId);

        if(tracker == null) {
            if (currentSurvey == null) {
                // Rename this map.size >= 3
                if (true) {
                    surveyTrackers.put(chatId, new SurveyTracker(chatId, user));
//                    createButtons(chatId, "How many questions would you like to ask? (Choose 1-3).",
//                            List.of(createButton("1", "qBtn1"), createButton("2", "qBtn2"),
//                                    createButton("3", "qBtn3")));
                    sendMessage(chatId, "How many questions would you like to ask? (Choose 1-3)");
                } else {
                    sendMessage(chatId, "You need at least 3 members to create a survey.");
                }
            }else{
                sendMessage(chatId, "A survey is already in progress. Please wait until it is completed.");
            }
        }else {
            if(tracker.getNumberOfQuestions() == 0) {
                handleQuestionCount(chatId, tracker, message);
            } else if (tracker.isWaitingForAnswerCount()) {
                handleAnswerCount(chatId, tracker, message);
            } else if (tracker.isWaitingForQuestionText()) {
                handleQuestionText(chatId, tracker, message);
            } else if (tracker.isWaitingForAnswerText()) {
                handleAnswerText(chatId, tracker, message);
            }else {
                if(!SurveyTracker.isSurveyIsStarted() || currentSurvey == null) {
                    handleSeconds(chatId, tracker, message);
                }
            }
        }
    }

    private void handleQuestionCount(Long chatId, SurveyTracker tracker, String messageText) {
        try {
            int questionCount = Integer.parseInt(messageText);
            if (questionCount >= 1 && questionCount <= 3) {
                tracker.setNumberOfQuestions(questionCount);
                sendMessage(chatId, "How many answers should the first question have? (Choose 2-4).");
//                createButtons(chatId, "How many answers should the first question have? (Choose 2-4).",
//                        List.of(createButton("1", "qBtn1"), createButton("2", "qBtn2"),
//                                createButton("3", "qBtn3")));
                tracker.setWaitingForAnswerCount(true);
            } else {
                sendMessage(chatId, "Enter a valid number of questions! (Choose 1-3)");
            }
        }catch (NumberFormatException e) {
            sendMessage(chatId, "Please enter valid number!");
        }
    }

    private void handleSeconds(Long chatId, SurveyTracker tracker, String messageText) {
        try {
            int second = Integer.parseInt(messageText);
            Thread.sleep(second * 1000L);
            SurveyTracker.setSurveyStart(true);
            // Start the survey not immediately
            startSurvey(chatId, tracker);
        }catch (NumberFormatException e) {
            sendMessage(chatId, "Please enter a number!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAnswerCount(Long chatId, SurveyTracker tracker, String messageText) {
        try {
            int answerCount = Integer.parseInt(messageText);
            if(answerCount >= 2 && answerCount <= 4) {
                tracker.setAnswerCountForCurrentQuestion(answerCount);
                tracker.setWaitingForAnswerCount(false);
                sendMessage(chatId, "Please enter the text for question " + tracker.getCurrentQuestionNumber() + ":");
//                createButtons(chatId, "How many answers should the first question have? (Choose 2-4).",
//                        List.of(createButton("2", "qBtn1"), createButton("3", "qBtn2"),
//                                createButton("4", "qBtn3")));
                tracker.setWaitingForQuestionText(true);
            }else {
                sendMessage(chatId, "Enter a valid number of answers! Choose(2-4)");
            }
        }catch (NumberFormatException e) {
            sendMessage(chatId, "Please enter a valid number!");
        }
    }

    private void handleQuestionText(Long chatId, SurveyTracker tracker, String messageText) {
        this.question = messageText;

        int currentQuestion = tracker.getCurrentQuestionNumber();

        tracker.setWaitingForAnswerText(true);
        tracker.setWaitingForQuestionText(false);
        sendMessage(chatId, "Write an answer for question " +  (currentQuestion) + ": ");

    }

    private void handleAnswerText(Long chatId, SurveyTracker tracker, String messageText) {
        options.add(messageText);

        int currentAnswer = options.size();
        if(currentAnswer < tracker.getAnswersCount()) {
            sendMessage(chatId, "Please write another answer for question " + tracker.getCurrentQuestionNumber() + ": ");
            tracker.setWaitingForAnswerText(true);
        }else {
            tracker.addQuestion(this.question, new ArrayList<>(options));
            options.clear();

            if (tracker.getCurrentQuestionNumber() < tracker.getNumberOfQuestions()) {
                sendMessage(chatId, "How many answers should question " + (tracker.getCurrentQuestionNumber() + 1) + " have? (Choose 2-4)");
                tracker.setWaitingForAnswerCount(true);
            } else {
                if(SurveyTracker.isSurveyIsStarted()) {
                    // TODO: Do something
                    // surveyTrackers.remove(chatId);
                }else {
                    createButtons(chatId, "Survey creation complete! Would you like to start the survey now?",
                            List.of(createButton("Yes", "start1"), createButton("No", "timeStart")));

                }
            }

            tracker.setWaitingForAnswerText(false);
        }
    }

    private void startSurvey(Long chatId, SurveyTracker tracker) {
        currentSurvey = tracker.toSurvey();
        currentSurvey.setCreator(users_map.get(chatId));
        sendMessageToAllUsers(currentSurvey.toString());
        sendMessageToAllUsers("Write an option for questions !");
        sendMessageToAllUsers("For example question [1] answers [1] You can only answer one time on any question ");

    }

    private void sendMessageToAllUsers(String msg) {
        for(Long chatId : users_map.keySet()) {
            sendMessage(chatId, msg);
        }
    }

    private InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }



    private void sendMessage(Long chatId, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(msg);
        try {
            execute(message);
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    /**
     *   InlineKeyboardMarkup container = new InlineKeyboardMarkup();
     *   List<List<InlineKeyboardButton>> buttonsGrid = new ArrayList<>();
     *   List<InlineKeyboardButton> firsRow = new ArrayList<>();
     *   InlineKeyboardButton button1 = new InlineKeyboardButton();
     *   button1.setText("CreateSurvey");
     *   button1.setCallbackData("1");
     *   firsRow.add(button1);
     *   buttonsGrid.add(firsRow);
     *
     *   SendMessage message1 = new SendMessage();
     *   message1.setText("Choose");
     *   message1.setChatId(String.valueOf(update.getMessage().getChatId()));
     *   container.setKeyboard(buttonsGrid);
     *   message1.setReplyMarkup(container);
     *   execute(message1);
     * */
    private void createButtons(Long chatId, String buttonsText, List<InlineKeyboardButton> buttons) {
        InlineKeyboardMarkup container = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttonsGrid = new ArrayList<>();

        buttonsGrid.add(buttons);

        SendMessage message1 = new SendMessage();
        message1.setText(buttonsText);
        message1.setChatId(String.valueOf(chatId));
        container.setKeyboard(buttonsGrid);
        message1.setReplyMarkup(container);
        try {
            execute(message1);
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "SAiNT";
    }

    public String getBotToken() {
        return "6687164541:AAFpRl-iV_gRvFJn_3QS8ZPsg6De1tUXTeI";
    }
}
