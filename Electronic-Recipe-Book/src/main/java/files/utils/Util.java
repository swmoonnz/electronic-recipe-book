package uc.seng301.asg1.utils;

public class Util {

    /**
     * Checks whether a given name for an ingredient is valid
     * @param name an ingredient name to be checked
     * @return true if given name contains only letters with or without white space
     */
    public static boolean isNameValid(String name) {
        return null != name && !name.isEmpty()
                && name.chars().allMatch(i -> Character.isAlphabetic(i) || Character.isWhitespace(i));
    }

}
