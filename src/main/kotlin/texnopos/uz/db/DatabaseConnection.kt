package texnopos.uz.db

import org.ktorm.database.Database

object DatabaseConnection {
    val database = Database.connect(
        url = "jdbc:mysql://kutnpvrhom7lki7u.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/qn6trquo8531ak77",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "isz026qn31imlm5z",
        password = "kzcj4nl07buatqkz"
    )
}