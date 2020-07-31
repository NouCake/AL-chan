package com.zen.alchan.helper.enums

import com.zen.alchan.R
import com.zen.alchan.helper.pojo.ColorPalette

enum class AppColorTheme(val value: ColorPalette) {
    DEFAULT_THEME_YELLOW(ColorPalette(R.color.yellow, R.color.cyan, R.color.magentaDark)),
    DEFAULT_THEME_GREEN(ColorPalette(R.color.green, R.color.lavender, R.color.brown)),
    DEFAULT_THEME_BLUE(ColorPalette(R.color.blue, R.color.cream, R.color.gold)),
    DEFAULT_THEME_PINK(ColorPalette(R.color.pink, R.color.sunshine, R.color.jade)),
    DEFAULT_THEME_RED(ColorPalette(R.color.red, R.color.aloevera, R.color.purple)),
    LIGHT_THEME_YELLOW(ColorPalette(R.color.lightThemeYellow, R.color.lightThemeCyan, R.color.lightThemeMagentaDark)),
    LIGHT_THEME_GREEN(ColorPalette(R.color.green, R.color.lavender, R.color.brown)),
    LIGHT_THEME_BLUE(ColorPalette(R.color.blue, R.color.cream, R.color.gold)),
    LIGHT_THEME_PINK(ColorPalette(R.color.pink, R.color.sunshine, R.color.jade)),
    LIGHT_THEME_RED(ColorPalette(R.color.red, R.color.aloevera, R.color.purple)),
    DARK_THEME_YELLOW(ColorPalette(R.color.yellow, R.color.cyan, R.color.magentaDark)),
    DARK_THEME_GREEN(ColorPalette(R.color.green, R.color.lavender, R.color.brown)),
    DARK_THEME_BLUE(ColorPalette(R.color.blue, R.color.cream, R.color.gold)),
    DARK_THEME_PINK(ColorPalette(R.color.pink, R.color.sunshine, R.color.jade)),
    DARK_THEME_RED(ColorPalette(R.color.red, R.color.aloevera, R.color.purple)),
}