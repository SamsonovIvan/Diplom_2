public class UserData {
    private String email;
    private String password;

    public UserData(String email, String password){
        this.email = email;
        this.password = password;
    }

    public static UserData from (User user){
        return new UserData(user.getEmail(), user.getPassword());
    }

    @Override
    public String toString(){
        return "UserData{" +
                "email='" + email + '\'' +
                ", password+'" + password + '\'' +
                '}';
    }
}
