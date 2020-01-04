package verif;

public class FormatText {

    public String formatUserService(String userService)
    {
        String format = userService.toLowerCase();
        char [] ltr = format.toCharArray();
        int cpt=0;

        for (int i=cpt ; i<format.length(); i++){
            while (ltr[i] == ' ' || ltr[i] =='_' || ltr[i] =='-' || ltr[i]  == '\\' || ltr[i]  == '/')
            {
                cpt=i+1;
                ltr[i] = ltr[cpt];
                i=cpt;
            }
            System.out.print(ltr[i]);
        }

        return ltr.toString();
    }

}
