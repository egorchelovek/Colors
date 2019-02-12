package com.joyfulmagic.colors.utils;

/**
 * Color Harmonizer calculates different variants
 * of harmonies of selected color.
 */
public class ColorHarmonizer {

    /**
     * Get harmony from color.
     * @param color selected color
     * @param type type of harmony index
     * @return array of harmonic colors with initial color
     */
    public static int[] getHarmony(int color, int type){
        int [] harmony = null;
        switch (type){
            default: harmony = getRainbow(color);
                break;
            case 1: harmony = getCompletary(color);
                break;
            case 2: harmony = getAnalogousUncentred(color);
                // little bit replacing of colors for more beautiful view
                if(false) {
                    int[] replaceHarmony = new int[5];
                    replaceHarmony[0] = harmony[3];
                    replaceHarmony[1] = harmony[4];
                    replaceHarmony[2] = harmony[0];
                    replaceHarmony[3] = harmony[1];
                    replaceHarmony[4] = harmony[2];
                    harmony = replaceHarmony;
                }
                break;
            case 3: harmony = getTriadic(color);
                break;
            case 4: harmony = getSplitComplementary(color);
                break;
            case 5: harmony = getTetradic(color);
                break;
            case 6: harmony = getSquare(color);
                break;
        }
        return harmony;
    }

    // right harmonies
    public static int[] getCompletary(int color){
        return getRightHarmony(color, 2, null);
    }
    public static int[] getTriadic(int color){
        return getRightHarmony(color, 3, null);
    }
    public static int[] getSquare(int color){
        return getRightHarmony(color, 4, null);
    }
    public static int[] getRainbow(int color){ return getRightHarmony(color, 7, null); }

    // another harmonies
    public static int[] getAnalogous(int color){
        int [] idxs = {1, 2, 10, 11};
        return getRightHarmony(color, 12, idxs);
    }
    public static int[] getAnalogousUncentred(int color){
        int [] idxs = {1, 2};
        return getRightHarmony(color, 12, idxs);
    }
    public static int[] getSplitComplementary(int color){
        int [] idxs = {5, 7};
        return getRightHarmony(color, 12, idxs);
    }
    public static int[] getTetradic(int color){
        int [] idxs = {2, 6, 8};
        return getRightHarmony(color, 12, idxs);
    }

    /**
     * Main Harmony Function
     * Returns right harmony if idxSet == null,
     * else may return another type of harmony...
     * @param color seed color
     * @param dimen division number
     * @param idxSet indexes
     * @return array of colors of harmony
     */
    private static int[] getRightHarmony(int color, int dimen, int[] idxSet){

        int size = dimen;
        if(idxSet != null) {
            size = idxSet.length + 1;
        }
        int[] colors = new int[size];
        float step = 1.0f / dimen;

        float [] hsl = ColorConverter.colorToHsl(color);

        int idxSetIdx = 0;

        colors[0] = color;
        for(int i = 1; i < dimen; i++){

            hsl[0] = getNextTone(step, hsl[0]);

            color = ColorConverter.hslToColor(hsl);

            int j = 0;
            if(idxSet == null) {
                j = i;
                colors[j] = color;
            } else if(idxSet[idxSetIdx] == i){
                    j = idxSetIdx + 1;
                colors[j] = color;
                    idxSetIdx++;
                    if(idxSetIdx >= idxSet.length) break;
            }
        }

        return colors;
    }
    // little happy function
    private static float getNextTone(float step, float tone){
        tone += step;
        if(tone > 1){
            tone -= 1;
        }
        return tone;
    }

    public static int [] getComplementaryMaxContrast(int color){
        int [] complementary = getCompletary(color);
        float [] hsl1 = ColorConverter.colorToHsl(complementary[1]);
        hsl1[2] = 0.5f;
        complementary[1] = ColorConverter.hslToColor(hsl1);
        return complementary;
    }
}
