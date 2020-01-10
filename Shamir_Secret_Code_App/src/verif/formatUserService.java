package verif;

public class formatUserService {

    public String formatUserService(String userService)
    {
        String format = userService.toLowerCase();
        char [] ltr = format.toCharArray();
        int cpt=0;
        userService="" ;

        for (int i=cpt ; i<format.length(); i++){
            while (ltr[i] == ' ' || ltr[i] =='_' || ltr[i] =='-' || ltr[i]  == '\\' || ltr[i]  == '/')
            {
                cpt=i+1;
                ltr[i] = ltr[cpt];
                i=cpt;
            }
            userService +=ltr[i];
        }
        return userService;
    }

}
