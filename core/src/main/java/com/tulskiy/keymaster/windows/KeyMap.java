/*
 * Copyright (c) 2011 Denis Tulskiy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with this work.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tulskiy.keymaster.windows;

import static java.awt.event.KeyEvent.VK_COMMA;
import static java.awt.event.KeyEvent.VK_DELETE;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_INSERT;
import static java.awt.event.KeyEvent.VK_MINUS;
import static java.awt.event.KeyEvent.VK_PERIOD;
import static java.awt.event.KeyEvent.VK_PLUS;
import static java.awt.event.KeyEvent.VK_PRINTSCREEN;
import static java.awt.event.KeyEvent.VK_SEMICOLON;
import static java.awt.event.KeyEvent.VK_SLASH;
import static java.awt.event.KeyEvent.VK_F13;
import static java.awt.event.KeyEvent.VK_F14;
import static java.awt.event.KeyEvent.VK_F15;
import static java.awt.event.KeyEvent.VK_F16;
import static java.awt.event.KeyEvent.VK_F17;
import static java.awt.event.KeyEvent.VK_F18;
import static java.awt.event.KeyEvent.VK_F19;
import static java.awt.event.KeyEvent.VK_F20;
import static java.awt.event.KeyEvent.VK_F21;
import static java.awt.event.KeyEvent.VK_F22;
import static java.awt.event.KeyEvent.VK_F23;
import static java.awt.event.KeyEvent.VK_F24;

import com.sun.jna.platform.win32.Win32VK;
import com.sun.jna.platform.win32.WinUser;
import com.tulskiy.keymaster.common.HotKey;

import java.awt.event.InputEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.KeyStroke;

/**
 * Author: Denis Tulskiy
 * Date: 6/20/11
 */
class KeyMap {
    private static final Map<Integer, Integer> codeExceptions = new HashMap<Integer, Integer>() {{
        put(VK_INSERT, 0x2D);
        put(VK_DELETE, 0x2E);
        put(VK_ENTER, 0x0D);
        put(VK_COMMA, 0xBC);
        put(VK_PERIOD, 0xBE);
        put(VK_PLUS, 0xBB);
        put(VK_MINUS, 0xBD);
        put(VK_SLASH, 0xBF);
        put(VK_SEMICOLON, 0xBA);
        put(VK_PRINTSCREEN, 0x2C);
        put(VK_F13, 0x7C);
        put(VK_F14, 0x7D);
        put(VK_F15, 0x7E);
        put(VK_F16, 0x7F);
        put(VK_F17, 0x80);
        put(VK_F18, 0x81);
        put(VK_F19, 0x82);
        put(VK_F20, 0x83);
        put(VK_F21, 0x84);
        put(VK_F22, 0x85);
        put(VK_F23, 0x86);
        put(VK_F24, 0x87);
    }};

    static int getCode(HotKey hotKey) {
        if (hotKey.isMedia()) {
            int code = 0;
            switch (hotKey.mediaKey) {
                case MEDIA_NEXT_TRACK:
                    code = Win32VK.VK_MEDIA_NEXT_TRACK.code;
                    break;
                case MEDIA_PLAY_PAUSE:
                    code = Win32VK.VK_MEDIA_PLAY_PAUSE.code;
                    break;
                case MEDIA_PREV_TRACK:
                    code = Win32VK.VK_MEDIA_PREV_TRACK.code;
                    break;
                case MEDIA_STOP:
                    code = Win32VK.VK_MEDIA_STOP.code;
                    break;
            }

            return code;
        } else {
            KeyStroke keyStroke = hotKey.keyStroke;
            Integer code = codeExceptions.get(keyStroke.getKeyCode());
            if (code != null) {
                return code;
            } else
                return keyStroke.getKeyCode();
        }
    }

    static int getModifiers(KeyStroke keyCode) {
        int modifiers = 0;
        if (keyCode != null) {
            if ((keyCode.getModifiers() & InputEvent.SHIFT_DOWN_MASK) != 0) {
                modifiers |= WinUser.MOD_SHIFT;
            }
            if ((keyCode.getModifiers() & InputEvent.CTRL_DOWN_MASK) != 0) {
                modifiers |= WinUser.MOD_CONTROL;
            }
            if ((keyCode.getModifiers() & InputEvent.META_DOWN_MASK) != 0) {
                modifiers |= WinUser.MOD_WIN;
            }
            if ((keyCode.getModifiers() & InputEvent.ALT_DOWN_MASK) != 0) {
                modifiers |= WinUser.MOD_ALT;
            }
        }

        String os = System.getProperty("os.version", "");
        // MOD_NOREPEAT only supported starting with Windows 7
        if (os.compareTo("6.1") >= 0) {
            modifiers |= WinUser.MOD_NOREPEAT;
        }

        return modifiers;
    }
}
