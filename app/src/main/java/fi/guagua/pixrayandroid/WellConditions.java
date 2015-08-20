package fi.guagua.pixrayandroid;

/**
 *
 */
public class WellConditions {
        private String mName;
        private String mWcClass;
        private String mConcentration;
        private String mUnits;
        private String mPH;

        public WellConditions(String name, String wcClass, String concentration,
                              String units, String ph) {
            mName = name;
            mWcClass = wcClass;
            mConcentration = concentration;
            mUnits = units;
            mPH = ph;
        }

        public String getName() {
            return mName;
        }

        public String getWcClass() {
            return mWcClass;
        }

        public String getConcentration() {
            return mConcentration;
        }

        public String getUnits() {
            return mUnits;
        }

        public String getPH() {
            return mPH;
        }

}
