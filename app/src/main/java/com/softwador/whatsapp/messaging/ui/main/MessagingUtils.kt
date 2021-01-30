package com.softwador.whatsapp.messaging.ui.main

class MessagingUtils {
    companion object {

        var MESSAGE_START: String = "היי "
        var COMMA: String = ","
        var JOB_DESCRIPTION_HEADER: String = "זוהי הצעת מחיר עבור:"
        var JOB_PRICE_HEADER: String = "מחיר:"
        var NEW_LINE = "\n"
        var MESSAGE_END: String = "אנא צור קשר עם המזכירה שלי לתיאום הגעה" + NEW_LINE + "רוב תודות"


        fun buildMessage(name: String, jobDescription: String, jobPrice: String): String {
            val string: java.lang.StringBuilder = StringBuilder()
            string.append(MESSAGE_START)
                .append(name)
                .append(COMMA)
                .append(NEW_LINE)
                .append(JOB_DESCRIPTION_HEADER)
                .append(NEW_LINE)
                .append(jobDescription)
                .append(NEW_LINE)
                .append(JOB_PRICE_HEADER)
                .append(NEW_LINE)
                .append(jobPrice)
                .append(NEW_LINE)
                .append(MESSAGE_END)

            return string.toString()
        }


    }
}