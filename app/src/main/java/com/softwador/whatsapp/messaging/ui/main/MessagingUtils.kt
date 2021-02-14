package com.softwador.whatsapp.messaging.ui.main

class MessagingUtils {
    companion object {

        var HEADER = "הצעת מחיר"
        var CUSTOMER_NAME_HEADER = "שם הלקוח: "
        var COMMA: String = ","
        var JOB_DESCRIPTION_HEADER: String = "עבור: "
        var JOB_PRICE_HEADER: String = "מחיר: "
        var JOB_PRICE_SUFFIX: String = " שח כולל מע\"מ."
        var SPACE: String = " ";
        var NEW_LINE = "\n"
        var DOT = "."
        var MESSAGE_END: String = "לשירותך בכל עת, \n" +
                "מנעולן מיכאל נעים."
        var PHONE_NUMBER: String = "0526723134"

        var BUSINESS_CARD_MESSAGE = "לקוח יקר! \n" +
                "תודה שפנית אליי. \n" +
                "אני מקווה שהצלחתי לעזור לך ולהעניק לך את היעוץ הטוב ביותר, \n" +
                "אשמח לעמוד לשירותך גם בעתיד, \n" +
                "בברכה, \n" +
                "מנעולן מיכאל נעים" +
                "\n" +
                PHONE_NUMBER


        fun buildMessage(name: String, jobDescription: String, jobPrice: String): String {
            val string: java.lang.StringBuilder = StringBuilder()
            string
                .append(HEADER)
                .append(NEW_LINE)
                .append(CUSTOMER_NAME_HEADER)
                .append(name)
                .append(NEW_LINE)
                .append(JOB_DESCRIPTION_HEADER)
                .append(jobDescription)
                .append(DOT)
                .append(NEW_LINE)
                .append(JOB_PRICE_HEADER)
                .append(jobPrice)
                .append(JOB_PRICE_SUFFIX)
                .append(NEW_LINE)
                .append(MESSAGE_END)
                .append(NEW_LINE)
                .append(PHONE_NUMBER)

            return string.toString()
        }


    }
}