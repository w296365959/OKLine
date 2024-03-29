package com.vboss.okline.zxing.result;

import com.google.zxing.client.result.ISBNParsedResult;

/**
 * Created by hupei on 2016/8/12.
 */
public class ISBNResult extends Result {
    private final String isbn;

    public ISBNResult(ISBNParsedResult isbnParsedResult) {
        this.isbn = isbnParsedResult.getISBN();
    }

    public String getISBN() {
        return isbn;
    }
}
