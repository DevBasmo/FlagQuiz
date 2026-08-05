package com.example.flagquizgame;

import  android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.button.MaterialButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.view.WindowManager;
import android.widget.GridLayout;
import android.service.autofill.ImageTransformation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.ImageView;
import android.widget.Button;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Menu;
import android.view.LayoutInflater;
import android.util.Log;
import android.os.Handler;
import android.graphics.drawable.Drawable;
import android.content.res.AssetManager;
import android.content.DialogInterface;
import android.content.Context;
import android.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class PlayButtonClass extends AppCompatActivity {

    private static final String TAG = "FlagQuizGame Activity";

    private List<String> fileNameList;
    private List<String> quizCountriesList;
    private Map<String, Boolean> regionsMap;
    private String correctAnswer;
    private int totalGuesses;
    private int guessRows;
    private int correctAnswers;
    private Random random;
    private Handler handler;
    private Animation shakeAnimation;

    private TextView questionNumberTextView;
    private ImageView flagImageView;
    private GridLayout buttonContainer;
    int currentQuestion = 1;
    int totalQuestions;
    private SettingsManager settingsManager;
    private FrameLayout timerFrame;

    private String selectedRegionKey;

    private CircularProgressIndicator timerProgressRing;
    private TextView timerCountdownText;
    private CountDownTimer questionTimer;


    private ImageButton backButton;
    private ImageButton refreshButton;

    private MediaPlayer correctSound;
    private MediaPlayer wrongSound;

    private final int CHOICES_MENU_ID = Menu.FIRST;
    private final int REGIONS_MENU_ID = Menu.FIRST + 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_button);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });

        settingsManager = new SettingsManager(this);
        totalQuestions = settingsManager.getQuestionCount();

        correctSound = MediaPlayer.create(this, R.raw.correct_answer);
        wrongSound = MediaPlayer.create(this, R.raw.wrong_answer);

        correctSound.setVolume(0.7f, 0.7f);
        wrongSound.setVolume(0.7f, 0.7f);


        backButton = findViewById(R.id.backButton);
        refreshButton = findViewById(R.id.refreshButton);


        timerFrame = findViewById(R.id.timerFrame);
        timerProgressRing = findViewById(R.id.timerProgressRing);
        timerCountdownText = findViewById(R.id.timerCountdownText);


        fileNameList = new ArrayList<String>();

        quizCountriesList = new ArrayList<String>();

        regionsMap = new HashMap<String, Boolean>();
        guessRows = 1;

        random = new Random();

        handler = new Handler();

        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(3);

        String[] regionNames = getResources().getStringArray(R.array.regionList);

        String selectedRegion = getIntent().getStringExtra(
                ContinentSelectActivity.EXTRA_SELECTED_REGION);
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(getRegionTitle(selectedRegion));

        selectedRegionKey = selectedRegion;

        for (String region : regionNames)
            if(selectedRegion != null)
            {
                regionsMap.put(region, region.equals(selectedRegion));
            }
        else {
                regionsMap.put(region, true);
            }

        questionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);

        flagImageView = (ImageView) findViewById(R.id.flagImageView);

        buttonContainer =(GridLayout) findViewById(R.id.buttonContainer);


        backButton.setOnClickListener(v ->{
            finish();
        });

        refreshButton.setOnClickListener(v ->
        {
            finish();
            startActivity(getIntent());
        });

        questionNumberTextView.setText(
                getString(R.string.question_of_total, currentQuestion, totalQuestions)
        );





        resetQuiz();








    }
    private String getRegionTitle(String region) {
        if (region == null) return "World Countries";

        switch (region) {
            case "africa": return "Africa";
            case "asia": return "Asia";
            case "europe": return "Europe";
            case "north_america": return "North America";
            case "south_america": return "South America";
            case "oceania": return "Oceania";
            default: return "World Countries";
        }
    }


    private void resetQuiz()
    {




// Reset score text

        AssetManager assets = getAssets();
        fileNameList.clear();

        try {

                Set<String> regions = regionsMap.keySet();

                for ( String region : regions)
                {
                    if(regionsMap.get(region))
                    {
                    String[]paths = assets.list(region);

                    for(String path : paths)
                        fileNameList.add(path.replace(".png",""));
                }
            }
        }
        catch (IOException e)
        {
            Log.e(TAG, "Error loading image file names", e);
        }

        currentQuestion= 1;
        correctAnswers = 0;
        totalGuesses = 0;
        quizCountriesList.clear();

        int flagCounter = 1;
        int numberOfFlags = fileNameList.size();

        while ( flagCounter <= totalQuestions )
        {
            int randomIndex = random.nextInt(numberOfFlags);

            String fileName = fileNameList.get(randomIndex);

            if (!quizCountriesList.contains(fileName))
            {
                quizCountriesList.add(fileName);
                ++flagCounter;
            }

        }
        loadNextFlag();


    }
    private void loadNextFlag()
    {


        String nextImageName = quizCountriesList.remove(0);
        correctAnswer = nextImageName;
        String region = nextImageName.substring(0, nextImageName.indexOf('-'));
        AssetManager assets = getAssets();

        try (InputStream stream = assets.open(region + "/" + nextImageName + ".png")) {
            Drawable flag = Drawable.createFromStream(stream, nextImageName);
            flagImageView.setImageDrawable(flag);
        } catch (IOException e) {
            Log.e(TAG, "Missing/broken flag asset: " + region + "/" + nextImageName + ".png", e);

            if (!fileNameList.isEmpty()) {
                String replacement = fileNameList.get(random.nextInt(fileNameList.size()));
                if (!quizCountriesList.contains(replacement) && !replacement.equals(nextImageName)) {
                    quizCountriesList.add(0, replacement);
                    loadNextFlag();
                    return;
                }
            }
        }
        questionNumberTextView.setText(
                getResources().getString(R.string.question)+ " "+
                        currentQuestion + " "+
                        getResources().getString(R.string.of)+" "+
                        totalQuestions
        );
        buttonContainer.removeAllViews();

        Collections.shuffle(fileNameList);

        int correct = fileNameList.indexOf(correctAnswer);
        fileNameList.add(fileNameList.remove(correct));


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        int numberOfChoices = 4;

        for (int i = 0; i < numberOfChoices; i++) {

            Button newGuessButton = (Button) inflater.inflate(R.layout.guess_button, null);

            String fileName = fileNameList.get(i);
            newGuessButton.setText(getCountryName(fileName));

            newGuessButton.setOnClickListener(guessButtonListener);

            // spacing
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 1, 0, 1);

            newGuessButton.setLayoutParams(params);

            buttonContainer.addView(newGuessButton);
        }


    int correctIndex = random.nextInt(numberOfChoices);
    Button correctButton = (Button) buttonContainer.getChildAt(correctIndex);
    correctButton.setText(getCountryName(correctAnswer));


    startQuestionTimer();
    }

    private TableRow getTableRow(int row)
        {
            return(TableRow) buttonContainer.getChildAt(row);
        }

        private String getCountryName(String name)
        {
            return name.substring(name.indexOf('-')+1).replace('_', ' ');
        }

    private void submitGuess(MaterialButton guessButton) {
        if(questionTimer != null)
        {
            questionTimer.cancel();
        }

        String guess = guessButton.getText().toString();
        String answer = getCountryName(correctAnswer);
        ++totalGuesses;

        if (guess.equals(answer)) {
            if ( correctSound != null && settingsManager.isSoundEnabled())
            {
                if(correctSound.isPlaying())
                {
                    correctSound.seekTo(0);
                }
                correctSound.start();
            }
            ++correctAnswers;


            disableButtons();

            if (currentQuestion == totalQuestions) {
                showResultDialog();

                           } else {
                currentQuestion++;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextFlag();
                    }
                }, 1000);
            }

            // ✅ Set correct green without destroying drawable
            guessButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.correct_green)));
        } else {
            if(wrongSound != null && settingsManager.isSoundEnabled()) {
                if (wrongSound.isPlaying()) {
                    wrongSound.seekTo(0);
                }
                wrongSound.start();
            }
            flagImageView.startAnimation(shakeAnimation);




            // ✅ Set wrong red without destroying drawable
            guessButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.wrong_red)));
            disableButtons();

            if (currentQuestion == totalQuestions) {
                showResultDialog();


            } else {

                currentQuestion++;

                handler.postDelayed(() -> {loadNextFlag();}, 1000);
            }        }



    }
    private void showResultDialog() {
        saveQuizResult();
        double score = (100.0 * correctAnswers) / totalQuestions;

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_quiz_result, null);

        TextView taglineText = dialogView.findViewById(R.id.resultTaglineText);
        CircularProgressIndicator scoreRing = dialogView.findViewById(R.id.resultScoreRing);
        TextView percentText = dialogView.findViewById(R.id.resultScorePercentText);
        TextView correctValue = dialogView.findViewById(R.id.resultCorrectValue);
        TextView incorrectValue = dialogView.findViewById(R.id.resultIncorrectValue);
        MaterialButton playAgainButton = dialogView.findViewById(R.id.resultPlayAgainButton);
        MaterialButton exitButton = dialogView.findViewById(R.id.exitButton);

        int roundedScore = (int) Math.round(score);
        percentText.setText(roundedScore + "%");
        correctValue.setText(correctAnswers + "/" + totalQuestions);
        incorrectValue.setText(String.valueOf(totalQuestions - correctAnswers));

        int ringColor;
        String tagline;
        if (roundedScore >= 80) {
            ringColor = getResources().getColor(R.color.correct_green);
            tagline = "Excellent work";
        } else if (roundedScore >= 50) {
            ringColor = getResources().getColor(R.color.timer_amber);
            tagline = "Good effort";
        } else {
            ringColor = getResources().getColor(R.color.timer_red);
            tagline = "Keep practicing";
        }
        scoreRing.setIndicatorColor(ringColor);
        taglineText.setText(tagline);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // Animate the ring filling from 0 to the actual score
        ObjectAnimator ringAnimator = ObjectAnimator.ofInt(scoreRing, "progress", 0, roundedScore);
        ringAnimator.setDuration(800);
        ringAnimator.setInterpolator(new DecelerateInterpolator());

        playAgainButton.setOnClickListener(v -> {
            dialog.dismiss();
            resetQuiz();
        });

        exitButton.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(PlayButtonClass.this, homeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        dialog.show();

        if (dialog.getWindow() != null) {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            dialog.getWindow().setLayout(
                    (int) (screenWidth * 0.75),
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }

        ringAnimator.start();
    }
        private void disableButtons()
        {
            for (int i = 0; i < buttonContainer.getChildCount(); i++) {
                buttonContainer.getChildAt(i).setEnabled(false);
            }
}

public boolean onCreateOptionsMenu(Menu menu)
{
    super.onCreateOptionsMenu(menu);
    menu.add(Menu.NONE, CHOICES_MENU_ID, Menu.NONE, R.string.choices);
    menu.add(Menu.NONE, REGIONS_MENU_ID, Menu.NONE, R.string.regions);

    return true;
}


    private OnClickListener guessButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // Cast to MaterialButton
            submitGuess((com.google.android.material.button.MaterialButton) v);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(questionTimer != null)
        {
            questionTimer.cancel();
        }

        if (correctSound != null) {
            correctSound.release();
            correctSound = null;
        }

        if (wrongSound != null) {
            wrongSound.release();
            wrongSound = null;
        }
    }

    private void saveQuizResult()
    {
        String regionForStats = (selectedRegionKey != null) ? selectedRegionKey :  "all";

        QuizResult result = new QuizResult(
                regionForStats, correctAnswers, totalQuestions,totalGuesses,
                System.currentTimeMillis());

        java.util.concurrent.Executors.newSingleThreadExecutor().execute(
                ()->AppDatabase.getInstance(getApplicationContext()).quizResultDao().insert(result));





    }

    private void startQuestionTimer() {
        if (questionTimer != null) {
            questionTimer.cancel();
        }

        if (!settingsManager.isTimerEnabled()) {
            timerFrame.setVisibility(View.GONE);
            timerProgressRing.setVisibility(View.GONE);
            timerCountdownText.setVisibility(View.GONE);
            return;
        }

        int totalSeconds = settingsManager.getTimerSeconds();
        long totalMillis = totalSeconds * 1000L;

        timerFrame.setVisibility(View.VISIBLE);
        timerProgressRing.setVisibility(View.VISIBLE);
        timerCountdownText.setVisibility(View.VISIBLE);
        timerProgressRing.setMax((int) totalMillis);
        timerProgressRing.setProgress((int) totalMillis);
        timerProgressRing.setIndicatorColor(getResources().getColor(R.color.timer_blue));
        timerCountdownText.setText(String.valueOf(totalSeconds));

        questionTimer = new CountDownTimer(totalMillis, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerProgressRing.setProgress((int) millisUntilFinished);

                int secondsLeft = (int) Math.ceil(millisUntilFinished / 1000.0);
                timerCountdownText.setText(String.valueOf(secondsLeft));

                double fraction = (double) millisUntilFinished / totalMillis;
                int color;
                if (fraction > 0.5) {
                    color = getResources().getColor(R.color.timer_blue);
                } else if (fraction > 0.2) {
                    color = getResources().getColor(R.color.timer_amber);
                } else {
                    color = getResources().getColor(R.color.timer_red);
                }
                timerProgressRing.setIndicatorColor(color);
            }

            @Override
            public void onFinish() {
                timerProgressRing.setProgress(0);
                timerCountdownText.setText("0");
                handleTimeout();
            }
        };
        questionTimer.start();
    }

    private void handleTimeout() {
        ++totalGuesses;

        if (wrongSound != null && settingsManager.isSoundEnabled()) {
            if (wrongSound.isPlaying()) wrongSound.seekTo(0);
            wrongSound.start();
        }

        flagImageView.startAnimation(shakeAnimation);
        disableButtons();



        if (currentQuestion == totalQuestions) {
            showResultDialog();
        } else {
            currentQuestion++;
            handler.postDelayed(this::loadNextFlag, 1000);
        }
    }
}