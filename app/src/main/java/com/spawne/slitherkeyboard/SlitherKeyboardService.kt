package com.spawne.slitherkeyboard

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo

/**
 * Slither.io klavyesinin görselini taklit eden özel Android klavyesi (IME).
 *
 * - Mor "pressed" glow efekti: res/drawable/key_pressed.xml + key_selector.xml
 * - Mavi-mor arkaplan gradienti: res/drawable/keyboard_background.xml
 * - Tuş düzeni: res/xml/qwerty.xml
 */
class SlitherKeyboardService : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard
    private var isShifted = false

    override fun onCreateInputView(): View {
        keyboard = Keyboard(this, R.xml.qwerty)

        keyboardView = LayoutInflater.from(this)
            .inflate(R.layout.keyboard_view_layout, null) as KeyboardView

        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)
        keyboardView.isPreviewEnabled = false // slither.io'da büyütme balonu yok, sade tutuyoruz

        return keyboardView
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        keyboardView.keyboard = keyboard
        keyboardView.invalidateAllKeys()
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val ic = currentInputConnection ?: return

        when (primaryCode) {
            Keyboard.KEYCODE_DELETE -> {
                ic.deleteSurroundingText(1, 0)
            }
            Keyboard.KEYCODE_SHIFT -> {
                isShifted = !isShifted
                keyboard.isShifted = isShifted
                keyboardView.invalidateAllKeys()
            }
            -2 -> {
                // "!@#" tuşu: ileride sembol sayfası eklenebilir, şimdilik no-op
            }
            -4 -> {
                // "OK" tuşu: nickname onayı gibi davransın diye ENTER gönderiyoruz
                ic.performEditorAction(EditorInfo.IME_ACTION_DONE)
            }
            else -> {
                var code = primaryCode.toChar()
                if (isShifted) {
                    code = code.uppercaseChar()
                }
                ic.commitText(code.toString(), 1)
            }
        }
    }

    // KeyboardView.OnKeyboardActionListener'ın zorunlu diğer metodları
    override fun onPress(primaryCode: Int) {}
    override fun onRelease(primaryCode: Int) {}
    override fun onText(text: CharSequence?) {}
    override fun swipeLeft() {}
    override fun swipeRight() {}
    override fun swipeDown() {}
    override fun swipeUp() {}
}
