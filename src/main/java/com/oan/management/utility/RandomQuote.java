package com.oan.management.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oan.management.model.Quote;

import java.io.IOException;
import java.net.URL;

/**
 * @author Oan Stultjens
 * @since 23/02/2018
 */
public class RandomQuote {
    private String url;

    public RandomQuote() {
    }

    public RandomQuote(String url) {
        this.url = url;
    }

    /**
     * @param url an absolute URL giving the base location (JSON) of the quote
     * @return a Quote from the specified URL
     * @see Quote
     */
    public Quote getQuote(String url) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Quote quote = mapper.readValue(new URL(url), Quote.class);
            return quote;
        } catch (IOException e) {
            // This is just added so I could work offline in the train
            Quote quote = new Quote("A late start with motivation is better than an earlier start without any motivation", "Oan Stultjens", "motivation");
            return quote;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
