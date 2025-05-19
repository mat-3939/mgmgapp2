package com.example.mgmgapp.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component("priceFormatter")
public class PriceFormatter {

   /**
     * 価格を「¥1,000」のような形式で返す
     * @param price 金額（BigDecimal）
     * @return 通貨記号付きの文字列
     */
    public String format(BigDecimal price) {
        if (price == null) return "";
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.JAPAN);
        return formatter.format(price);
    }
}
