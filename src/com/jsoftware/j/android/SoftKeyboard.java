/*
 * Copyright (C) 2008-2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.jsoftware.j.android;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

/**
 * Example of writing an input method for a soft keyboard. This code is focused
 * on simplicity over completeness, so it should in no way be considered to be a
 * complete soft keyboard implementation. Its purpose is to provide a basic
 * example for how you would get started writing an input method, to be fleshed
 * out as appropriate.
 */
public class SoftKeyboard extends InputMethodService implements
  KeyboardView.OnKeyboardActionListener
{
  static final int KEYCODE_QWERTY_KEYBOARD = -1004;
  static final int KEYCODE_SYM1_KEYBOARD = -1001;
  static final int KEYCODE_SYM2_KEYBOARD = -1002;
  static final int KEYCODE_NUM_KEYBOARD = -1003;
  static final int KEYCODE_ENTER = 10;

  static final boolean DEBUG = false;

  /**
   * This boolean indicates the optional example code for performing
   * processing of hard keys in addition to regular text generation from
   * on-screen interaction. It would be used for input methods that perform
   * language translations (such as converting text entered on a QWERTY
   * keyboard to Chinese), but may not be used for input methods that are
   * primarily intended to be used for on-screen text entry.
   */
  static final boolean PROCESS_HARD_KEYS = true;
  protected boolean volDownPressed = false;

  private KeyboardView mInputView;
  // private CompletionInfo[] mCompletions;

  // private boolean mPredictionOn;
  // private boolean mCompletionOn;
  private int mLastDisplayWidth;
  private boolean mCapsLock;
  private long mLastShiftTime;
  // private long mMetaState;

  private LatinKeyboard mQwertyKeyboard;
  private LatinKeyboard mSym1Keyboard;
  private LatinKeyboard mSym2Keyboard;
  private LatinKeyboard mNumKeyboard;

  private LatinKeyboard mCurKeyboard;

  /**
   * Main initialization of the input method component. Be sure to call to
   * super class.
   */
  @Override
  public void onCreate()
  {
    super.onCreate();
  }

  /**
   * This is the point where you can do all of your UI initialization. It is
   * called after creation and any configuration change.
   */
  @Override
  public void onInitializeInterface()
  {
    if (mQwertyKeyboard != null) {
      // Configuration changes can happen after the keyboard gets
      // recreated,
      // so we need to be able to re-build the keyboards if the available
      // space has changed.
      int displayWidth = getMaxWidth();
      if (displayWidth == mLastDisplayWidth)
        return;
      mLastDisplayWidth = displayWidth;
    }
    mSym1Keyboard = new LatinKeyboard(this, R.xml.jsym1);
    mSym2Keyboard = new LatinKeyboard(this, R.xml.jsym2);
    mQwertyKeyboard = new LatinKeyboard(this, R.xml.qwerty);
    mNumKeyboard = new LatinKeyboard(this, R.xml.num);
  }

  /**
   * - * Called by the framework when your view for creating input needs to be
   * generated. This will be called the first time your input method is
   * displayed, and every time it needs to be re-created such as due to a
   * configuration change.
   */

  @Override
  public View onCreateInputView()
  {
    mInputView = (KeyboardView) getLayoutInflater().inflate(R.layout.input,
                 null);
    mInputView.setOnKeyboardActionListener(this);
    mInputView.setKeyboard(mSym1Keyboard);
    return mInputView;
  }

  /**
   * This is the main point where we do our initialization of the input method
   * to begin operating on an application. At this point we have been bound to
   * the client, and are now receiving all of the detailed information about
   * the target of our edits.
   */
  @Override
  public void onStartInput(EditorInfo attribute, boolean restarting)
  {
    super.onStartInput(attribute, restarting);
    mCurKeyboard = mSym1Keyboard;
    updateShiftKeyState(attribute);
    mCurKeyboard.setImeOptions(getResources(), attribute.imeOptions);
  }

  /**
   * This is called when the user is done editing a field. We can use this to
   * reset our state.
   */
  @Override
  public void onFinishInput()
  {
    super.onFinishInput();

    mCurKeyboard = mQwertyKeyboard;
    if (mInputView != null) {
      mInputView.closing();
    }
  }

  @Override
  public void onStartInputView(EditorInfo attribute, boolean restarting)
  {
    super.onStartInputView(attribute, restarting);
    // Apply the selected keyboard to the input view.
    mInputView.setKeyboard(mCurKeyboard);
    mInputView.closing();
  }

  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event)
  {
    if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
      volDownPressed = false;
      return true;
    } else {
      return super.onKeyUp(keyCode, event);
    }
  }
  /**
   * Use this to monitor key events being delivered to the application. We get
   * first crack at them, and can either resume them or let them continue to
   * the app.
   */
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event)
  {

    // trying to make control characters with vol down as ctl key
    if(volDownPressed && Character.isLetter(keyCode)) {
      int n = Character.toLowerCase(keyCode);
      keyDownUp(n-96);
      return true;
    }

    switch (keyCode) {
    case KeyEvent.KEYCODE_BACK:
      // The InputMethodService already takes care of the back
      // key for us, to dismiss the input method if it is shown.
      // However, our keyboard could be showing a pop-up window
      // that back should dismiss, so we first allow it to do that.
      if (event.getRepeatCount() == 0 && mInputView != null) {
        if (mInputView.handleBack()) {
          return true;
        }
      }
      break;

    case KeyEvent.KEYCODE_DEL:
      break;

    case KeyEvent.KEYCODE_ENTER:
      // Let the underlying text editor always handle these.
      return false;

//		case KeyEvent.k
    case KeyEvent.KEYCODE_VOLUME_DOWN:
      volDownPressed = true;
      return true;

    case KeyEvent.KEYCODE_VOLUME_UP:
      volDownPressed = false;
      return true;

    default:
      if (PROCESS_HARD_KEYS) {
        if (keyCode == KeyEvent.KEYCODE_SPACE
            && (event.getMetaState() & KeyEvent.META_ALT_ON) != 0) {
          // A silly example: in our input method, Alt+Space
          // is a shortcut for 'android' in lower case.
          InputConnection ic = getCurrentInputConnection();
          if (ic != null) {
            // First, tell the editor that it is no longer in the
            // shift state, since we are consuming this.
            ic.clearMetaKeyStates(KeyEvent.META_ALT_ON);
            keyDownUp(KeyEvent.KEYCODE_A);
            keyDownUp(KeyEvent.KEYCODE_N);
            keyDownUp(KeyEvent.KEYCODE_D);
            keyDownUp(KeyEvent.KEYCODE_R);
            keyDownUp(KeyEvent.KEYCODE_O);
            keyDownUp(KeyEvent.KEYCODE_I);
            keyDownUp(KeyEvent.KEYCODE_D);
            // And we consume this event.
            return true;
          }
        }
      }
    }

    return super.onKeyDown(keyCode, event);
  }

  /**
   * Helper to update the shift state of our keyboard based on the initial
   * editor state.
   */
  private void updateShiftKeyState(EditorInfo attr)
  {
    if (attr != null && mInputView != null
        && mQwertyKeyboard == mInputView.getKeyboard()) {
      int caps = 0;
      EditorInfo ei = getCurrentInputEditorInfo();
      if (ei != null && ei.inputType != EditorInfo.TYPE_NULL) {
        caps = getCurrentInputConnection().getCursorCapsMode(
                 attr.inputType);
      }
      mInputView.setShifted(mCapsLock || caps != 0);
    }
  }

  /**
   * Helper to send a key down / key up pair to the current editor.
   */
  private void keyDownUp(int keyEventCode)
  {
    getCurrentInputConnection().sendKeyEvent(
      new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
    getCurrentInputConnection().sendKeyEvent(
      new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
  }

  public void onKey(int primaryCode, int[] keyCodes)
  {
    if (primaryCode == KEYCODE_QWERTY_KEYBOARD) {
      mInputView.setKeyboard(mQwertyKeyboard);
    } else if (primaryCode == KEYCODE_SYM1_KEYBOARD) {
      mInputView.setKeyboard(mSym1Keyboard);
    } else if (primaryCode == KEYCODE_SYM2_KEYBOARD) {
      mInputView.setKeyboard(mSym2Keyboard);
    } else if (primaryCode == KEYCODE_NUM_KEYBOARD) {
      mInputView.setKeyboard(mNumKeyboard);
    } else if (primaryCode == 10) {
      handleEnter();
    } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
      handleBackspace();
    } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
      handleShift();
    } else {
      handleCharacter(primaryCode, keyCodes);
    }
  }

  public void onText(CharSequence text)
  {
    InputConnection ic = getCurrentInputConnection();
    if (ic == null)
      return;
    ic.beginBatchEdit();

    ic.commitText(text, 0);
    ic.endBatchEdit();
    updateShiftKeyState(getCurrentInputEditorInfo());
  }

  private void handleBackspace()
  {
    keyDownUp(KeyEvent.KEYCODE_DEL);
    updateShiftKeyState(getCurrentInputEditorInfo());
  }

  private void handleEnter()
  {
    keyDownUp(KeyEvent.KEYCODE_ENTER);
    updateShiftKeyState(getCurrentInputEditorInfo());
  }

  private void handleShift()
  {
    if (mInputView == null) {
      return;
    }

    Keyboard currentKeyboard = mInputView.getKeyboard();
    if (mQwertyKeyboard == currentKeyboard) {
      // Alphabet keyboard
      checkToggleCapsLock();
      mInputView.setShifted(mCapsLock || !mInputView.isShifted());
    }
  }

  private String translateSpecialCode(int primaryCode)
  {
    String result = "";
    switch (primaryCode) {
    case -1100:
      result = "A.";
      break;
    case -1101:
      result = "a.";
      break;
    case -1102:
      result = "a:";
      break;
    case -1103:
      result = "b.";
      break;
    case -1104:
      result = "C.";
      break;
    case -1105:
      result = "D.";
      break;
    case -1106:
      result = "D:";
      break;
    case -1107:
      result = "d.";
      break;
    case -1108:
      result = "E.";
      break;
    case -1109:
      result = "e.";
      break;
    case -1110:
      result = "f.";
      break;
    case -1111:
      result = "H.";
      break;
    case -1112:
      result = "I.";
      break;
    case -1113:
      result = "i.";
      break;
    case -1114:
      result = "i:";
      break;
    case -1115:
      result = "j.";
      break;
    case -1116:
      result = "L.";
      break;
    case -1117:
      result = "L:";
      break;
    case -1118:
      result = "M.";
      break;
    case -1119:
      result = "o.";
      break;
    case -1120:
      result = "p.";
      break;
    case -1121:
      result = "p..";
      break;
    case -1122:
      result = "p:";
      break;
    case -1123:
      result = "q:";
      break;
    case -1124:
      result = "r.";
      break;
    case -1125:
      result = "S:";
      break;
    case -1126:
      result = "s:";
      break;
    case -1127:
      result = "T.";
      break;
    case -1128:
      result = "t.";
      break;
    case -1129:
      result = "t:";
      break;
    case -1130:
      result = "u:";
      break;
    case -1131:
      result = "x:";
      break;
    case -1148:
      result = "NB. ";
      break;
    case -1150:
      result = "ar";
      break;
    case -1151:
      result = "ad";
      break;
    }

    return result;
  }

  private void handleCharacter(int primaryCode, int[] keyCodes)
  {
    InputConnection ic = getCurrentInputConnection();
    if (isInputViewShown()) {
      String s;
      if (primaryCode <= -1100) {
        s = translateSpecialCode(primaryCode);
      } else {
        if (mInputView.isShifted()
            && Character.isLowerCase(primaryCode)) {
          primaryCode = Character.toUpperCase(primaryCode);
        }
        s = String.valueOf((char) primaryCode);
      }
      ic.commitText(s, s.length());
    }

  }

  private void handleClose()
  {
    requestHideSelf(0);
    mInputView.closing();
  }

  private void checkToggleCapsLock()
  {
    long now = System.currentTimeMillis();
    if (mLastShiftTime + 800 > now) {
      mCapsLock = !mCapsLock;
      mLastShiftTime = 0;
    } else {
      mLastShiftTime = now;
    }
  }

  public void swipeLeft()
  {
  }

  public void swipeRight()
  {
  }

  public void swipeUp()
  {
  }

  public void swipeDown()
  {
    handleClose();
  }

  public void onPress(int primaryCode) { }

  public void onRelease(int primaryCode) { }
}
