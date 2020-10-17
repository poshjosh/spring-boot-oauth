package com.looseboxes.spring.oauth.facebook.profile.graphapi.v2_6;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

/**
 * @author hp
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgeRange implements Serializable{
    
    private Short min;
    private Short max;

    public Short getMin() {
        return min;
    }

    public void setMin(Short min) {
        this.min = min;
    }

    public Short getMax() {
        return max;
    }

    public void setMax(Short max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "AgeRange{" + "min=" + min + ", max=" + max + '}';
    }
}
