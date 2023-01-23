import net.datafaker.Faker;
public class UserGenerator {
    static Faker faker = new Faker();

    public static User getRandomUser(){
      String email = faker.internet().emailAddress();
      String password = faker.internet().password();
      String name = faker.name().firstName();
      return new User(email, password, name);
    }

    public static UserData getRandomUserData(){
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        return new UserData(email, password);
    }

    public static User getDefault(String email, String password, String name){
        return new User(email, password, name);
    }
}
