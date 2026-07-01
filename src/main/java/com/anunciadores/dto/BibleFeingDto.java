  package  com.anunciadores.dto;
  import com.anunciadores.dto.CountryDto;
  import com.anunciadores.dto.LanguageDto;
  import java.time.OffsetDateTime;
  import java.util.List;
  public class BibleFeingDto
  {
    private String id;
    private String dblID;
    private Object relatedDbl;
    private String name;
    private String nameLocal;
    private String abbreviation;
    private String abbreviationLocal;
    public String getId() {
return this.id;
    } private String description; private String descriptionLocal; private LanguageDto language; private List<CountryDto> countries; private String type; private OffsetDateTime updatedAt; private List<Object> audioBibles;
    public void setId(String id) {
this.id = id;
    }
    public String getDblID() {
return this.dblID;
    }
    public void setDblID(String dblID) {
this.dblID = dblID;
    }
    public Object getRelatedDbl() {
return this.relatedDbl;
    }
    public void setRelatedDbl(Object relatedDbl) {
this.relatedDbl = relatedDbl;
    }
    public String getName() {
return this.name;
    }
    public void setName(String name) {
this.name = name;
    }
    public String getNameLocal() {
return this.nameLocal;
    }
    public void setNameLocal(String nameLocal) {
this.nameLocal = nameLocal;
    }
    public String getAbbreviation() {
return this.abbreviation;
    }
    public void setAbbreviation(String abbreviation) {
this.abbreviation = abbreviation;
    }
    public String getAbbreviationLocal() {
return this.abbreviationLocal;
    }
    public void setAbbreviationLocal(String abbreviationLocal) {
this.abbreviationLocal = abbreviationLocal;
    }
    public String getDescription() {
return this.description;
    }
    public void setDescription(String description) {
this.description = description;
    }
    public String getDescriptionLocal() {
return this.descriptionLocal;
    }
    public void setDescriptionLocal(String descriptionLocal) {
this.descriptionLocal = descriptionLocal;
    }
    public LanguageDto getLanguage() {
return this.language;
    }
    public void setLanguage(LanguageDto language) {
this.language = language;
    }
    public List<CountryDto> getCountries() {
return this.countries;
    }
    public void setCountries(List<CountryDto> countries) {
this.countries = countries;
    }
    public String getType() {
return this.type;
    }
    public void setType(String type) {
this.type = type;
    }
    public OffsetDateTime getUpdatedAt() {
return this.updatedAt;
    }
    public void setUpdatedAt(OffsetDateTime updatedAt) {
this.updatedAt = updatedAt;
    }
    public List<Object> getAudioBibles() {
return this.audioBibles;
    }
    public void setAudioBibles(List<Object> audioBibles) {
this.audioBibles = audioBibles;
    }
  }
