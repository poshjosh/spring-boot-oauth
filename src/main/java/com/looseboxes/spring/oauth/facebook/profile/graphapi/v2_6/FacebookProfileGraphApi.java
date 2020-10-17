package com.looseboxes.spring.oauth.facebook.profile.graphapi.v2_6;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotBlank;

/**
 * @author hp
 * @see https://developers.facebook.com/docs/graph-api/reference/v2.6/user
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookProfileGraphApi implements Serializable{
    
    /**
     * Numeric string
     * The app user's App-Scoped User ID. This ID is unique to the app and 
     * cannot be used by other apps
     */
    @NotBlank
    private String id;
    
    /**
     * The age segment for this person expressed as a minimum and maximum age. 
     * For example, more than 18, less than 21.
     */
    private AgeRange age_range;
    
    /**
     * The person's birthday. This is a fixed format string, like MM/DD/YYYY. 
     * However, people can control who can see the year they were born separately 
     * from the month and day so this string can be only the year (YYYY) or the 
     * month + day (MM/DD)
     */
    private String birthday;
    
    /**
     * The User's primary email address listed on their profile. This field 
     * will not be returned if no valid email address is available.
     */
    private String email;
    
    private List<Experience> favorite_athletes = Collections.EMPTY_LIST;

    /**
     * Sports teams the User likes.
     */
    private List<Experience> favorite_teams = Collections.EMPTY_LIST;

    /**
     * The person's first name
     */
    private String first_name;
    
    /**
     * The gender selected by this person, male or female. If the gender is 
     * set to a custom value, this value will be based off of the preferred 
     * pronoun; it will be omitted if the preferred pronoun is neutral
     */
    private String gender;
    
    /**
     * The person's inspirational people
     */
    private List<Experience> inspirational_people = Collections.EMPTY_LIST;
    
    /**
     * Facebook Pages representing the languages this person
     */
    private List<Experience> languages = Collections.EMPTY_LIST;
    
    /**
     * The person's last name
     */
    private String last_name;
    
    /**
     * A link to the person's Timeline. The link will only resolve if the person 
     * clicking the link is logged into Facebook and is a friend of the person 
     * whose profile is being viewed.
     */
    private String link;
    
    /**
     * What the person is interested in meeting for
     */
    private List<String> meeting_for = Collections.EMPTY_LIST;
    
    /**
     * The person's middle name
     */
    private String middle_name;
    
    /**
     * The person's full name
     */
    private String name;
    
    /**
     * The profile picture URL of the Messenger user. The URL will expire.
     */
    private String profile_pic;
    
    /**
     * The person's favorite quotes
     */
    private String quotes;
    
    /**
     * The person's significant other
     */
    private FacebookProfileGraphApi significant_other;
    
    /**
     * Sports played by the person
     */
    private List<Experience> sports = Collections.EMPTY_LIST;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AgeRange getAge_range() {
        return age_range;
    }

    public void setAge_range(AgeRange age_range) {
        this.age_range = age_range;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Experience> getFavorite_athletes() {
        return favorite_athletes;
    }

    public void setFavorite_athletes(List<Experience> favorite_athletes) {
        this.favorite_athletes = favorite_athletes;
    }

    public List<Experience> getFavorite_teams() {
        return favorite_teams;
    }

    public void setFavorite_teams(List<Experience> favorite_teams) {
        this.favorite_teams = favorite_teams;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Experience> getInspirational_people() {
        return inspirational_people;
    }

    public void setInspirational_people(List<Experience> inspirational_people) {
        this.inspirational_people = inspirational_people;
    }

    public List<Experience> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Experience> languages) {
        this.languages = languages;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getMeeting_for() {
        return meeting_for;
    }

    public void setMeeting_for(List<String> meeting_for) {
        this.meeting_for = meeting_for;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getQuotes() {
        return quotes;
    }

    public void setQuotes(String quotes) {
        this.quotes = quotes;
    }

    public FacebookProfileGraphApi getSignificant_other() {
        return significant_other;
    }

    public void setSignificant_other(FacebookProfileGraphApi significant_other) {
        this.significant_other = significant_other;
    }

    public List<Experience> getSports() {
        return sports;
    }

    public void setSports(List<Experience> sports) {
        this.sports = sports;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FacebookProfileGraphApi other = (FacebookProfileGraphApi) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FacebookProfile{" + "id=" + id + ", age_range=" + age_range + 
                ", birthday=" + birthday + ", email=" + email + ", favorite_athletes=" + favorite_athletes + 
                ", favorite_teams=" + favorite_teams + ", first_name=" + first_name + ", gender=" + gender + 
                ", inspirational_people=" + inspirational_people + ", languages=" + languages + 
                ", last_name=" + last_name + ", link=" + link + ", meeting_for=" + meeting_for + 
                ", middle_name=" + middle_name + ", name=" + name + ", profile_pic=" + profile_pic + 
                ", quotes=" + quotes + ", significant_other=FacebookProfile{" + (significant_other == null ? null : significant_other.getId()) + 
                "}, sports=" + sports + '}';
    }
}
