package com.ziojio.zuiapp.service;

import android.inputmethodservice.InputMethodService;
import android.os.Build;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziojio.zuiapp.R;

import java.lang.reflect.Type;
import java.util.HashMap;

import timber.log.Timber;

public class FastInputIME extends InputMethodService implements View.OnClickListener {
    private int currentInputType = InputType.TYPE_CLASS_TEXT;
    private int upperFlag = 0; // 1 to Upper once, 2 keep upper state

    private static final HashMap<Integer, Character> numberMap = new HashMap<>();
    private static final HashMap<Integer, Character> charMap = new HashMap<>();

    static {
        numberMap.put(R.id.input_0, '0');
        numberMap.put(R.id.input_1, '1');
        numberMap.put(R.id.input_2, '2');
        numberMap.put(R.id.input_3, '3');
        numberMap.put(R.id.input_4, '4');
        numberMap.put(R.id.input_5, '5');
        numberMap.put(R.id.input_6, '6');
        numberMap.put(R.id.input_7, '7');
        numberMap.put(R.id.input_8, '8');
        numberMap.put(R.id.input_9, '9');

        charMap.put(R.id.input_a, 'a');
        charMap.put(R.id.input_b, 'b');
        charMap.put(R.id.input_c, 'c');
        charMap.put(R.id.input_d, 'd');
        charMap.put(R.id.input_e, 'e');
        charMap.put(R.id.input_f, 'f');
        charMap.put(R.id.input_g, 'g');
        charMap.put(R.id.input_h, 'h');
        charMap.put(R.id.input_i, 'i');
        charMap.put(R.id.input_j, 'j');
        charMap.put(R.id.input_k, 'k');
        charMap.put(R.id.input_l, 'l');
        charMap.put(R.id.input_m, 'm');
        charMap.put(R.id.input_n, 'n');
        charMap.put(R.id.input_o, 'o');
        charMap.put(R.id.input_p, 'p');
        charMap.put(R.id.input_q, 'q');
        charMap.put(R.id.input_r, 'r');
        charMap.put(R.id.input_s, 's');
        charMap.put(R.id.input_t, 't');
        charMap.put(R.id.input_u, 'u');
        charMap.put(R.id.input_v, 'v');
        charMap.put(R.id.input_w, 'w');
        charMap.put(R.id.input_x, 'x');
        charMap.put(R.id.input_y, 'y');
        charMap.put(R.id.input_z, 'z');
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("onCreate ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Timber.d("shouldOfferSwitchingToNextInputMethod=" + shouldOfferSwitchingToNextInputMethod());
        }
    }

    @Override
    public View onCreateInputView() {
        Timber.d("onCreateInputView ");
        return createInputView();
    }

    public View createInputView() {
        View inputView = getLayoutInflater().inflate(R.layout.fast_input_qwert, null);
        for (int key : charMap.keySet()) {
            inputView.findViewById(key).setOnClickListener(v -> {
                int id = v.getId();
                Character c = charMap.get(id);
                if (c != null) {
                    InputConnection input = getCurrentInputConnection();
                    String text = c.toString();
                    if (upperFlag > 0) {
                        text = text.toUpperCase();
                        if (upperFlag == 1) {
                            upperFlag = 0;
                            showCharUpper(inputView, false);
                        }
                    }
                    input.commitText(text, 1);
                }
            });
        }
        inputView.findViewById(R.id.input_upper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upperFlag = upperFlag > 0 ? 0 : 1;
                showCharUpper(inputView, upperFlag > 0);
            }
        });
        inputView.findViewById(R.id.input_upper).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                upperFlag = upperFlag > 0 ? 0 : 2;
                showCharUpper(inputView, upperFlag > 0);
                return true;
            }
        });
        inputView.findViewById(R.id.input_123).setOnClickListener(this);
        inputView.findViewById(R.id.input_space).setOnClickListener(this);
        inputView.findViewById(R.id.input_delete).setOnClickListener(this);
        inputView.findViewById(R.id.input_linebreak).setOnClickListener(this);
        inputView.findViewById(R.id.input_close).setOnClickListener(this);
        return inputView;
    }

    private View createInputNumberView() {
        View inputView = getLayoutInflater().inflate(R.layout.fast_input, null);
        for (int key : numberMap.keySet()) {
            inputView.findViewById(key).setOnClickListener(v -> {
                int id = v.getId();
                Character c = numberMap.get(id);
                if (c != null) {
                    InputConnection input = getCurrentInputConnection();
                    input.commitText(c.toString(), 1);
                }
            });
        }
        inputView.findViewById(R.id.input_delete).setOnClickListener(this);
        inputView.findViewById(R.id.input_done).setOnClickListener(this);
        inputView.findViewById(R.id.input_close).setOnClickListener(this);
        return inputView;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        Timber.d("onStartInputView info=" + info);
        Timber.d("onStartInputView restarting=" + restarting);
        int type = info.inputType & InputType.TYPE_MASK_CLASS;
        Timber.d("onStartInputView currentInputType=" + currentInputType);
        Timber.d("onStartInputView type=" + type);
        Timber.d("TYPE_CLASS_TEXT=" + InputType.TYPE_CLASS_TEXT + " TYPE_CLASS_NUMBER=" + InputType.TYPE_CLASS_NUMBER);
        if (type != currentInputType) {
            if (type == InputType.TYPE_CLASS_TEXT) {
                setInputView(createInputView());
            } else if (type == InputType.TYPE_CLASS_NUMBER) {
                setInputView(createInputNumberView());
            }
            currentInputType = type;
        }
    }

    @Override
    protected void onCurrentInputMethodSubtypeChanged(InputMethodSubtype newSubtype) {
        super.onCurrentInputMethodSubtypeChanged(newSubtype);
        Timber.d("onCurrentInputMethodSubtypeChanged newSubtype=" + newSubtype);
        Timber.d("onCurrentInputMethodSubtypeChanged getName=" + getString(newSubtype.getNameResId()));
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy ");
    }

    private void showCharUpper(View createdView, boolean isUpper) {
        TextView t;
        Character c;
        for (int key : charMap.keySet()) {
            t = createdView.findViewById(key);
            c = charMap.get(key);
            if (t != null && c != null) {
                t.setText(isUpper ? c.toString().toUpperCase() : c.toString());
            }
        }
        ImageView up = createdView.findViewById(R.id.input_upper_icon);
        if (isUpper) {
            up.setImageResource(R.drawable.input_uppercase_enable);
        } else {
            up.setImageResource(R.drawable.input_uppercase);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        InputConnection input = getCurrentInputConnection();
        if (id == R.id.input_close) {
            requestHideSelf(InputMethodManager.HIDE_NOT_ALWAYS);
        } else if (id == R.id.input_delete) {
            input.deleteSurroundingText(1, 0);
        } else if (id == R.id.input_space) {
            input.commitText(" ", 1);
        } else if (id == R.id.input_done) {
            requestHideSelf(InputMethodManager.HIDE_NOT_ALWAYS);
        } else if (id == R.id.input_linebreak) {
            input.commitText("\n", 1);
        } else if (id == R.id.input_123) {
            View view = createInputNumberView();
            TextView done = view.findViewById(R.id.input_done);
            done.setText("返回");
            done.setOnClickListener(view1 -> {
                setInputView(createInputView());
            });
            setInputView(view);
        }
    }
}
