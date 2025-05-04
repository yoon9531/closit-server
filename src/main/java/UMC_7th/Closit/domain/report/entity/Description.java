package UMC_7th.Closit.domain.report.entity;

public enum Description {
    SPAM("스팸"),
    ABUSE("욕설 및 비방"),
    FRAUD("사기 또는 허위 정보"),
    INAPPROPRIATE("부적절한 콘텐츠"),
    OTHER("기타");

    private final String description;

    Description(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
