package RedisORM.useTest;

import RedisORM.annotations.*;

import java.util.List;
import java.util.Set;

@RHash(id="othertype")
public class OtherType {

    @RKey
    public int id;

    @RString
    public String signatute;

    @RList
    public List<String> language;

    @RSet(sorted = true)
    public Set<String> education;

    @RSet
    public Set<String> phones;

    public OtherType() {
    }

    public OtherType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSignatute() {
        return signatute;
    }

    public void setSignatute(String signatute) {
        this.signatute = signatute;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public Set<String> getEducation() {
        return education;
    }

    public void setEducation(Set<String> education) {
        this.education = education;
    }

    public Set<String> getPhones() {
        return phones;
    }

    public void setPhones(Set<String> phones) {
        this.phones = phones;
    }
}
