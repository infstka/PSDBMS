package com.psmsdb.lr17;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLDB extends AsyncTask<String, String, String> {

    Connection connection;
    String LOG = "LogMessage";

    @Override
    protected String doInBackground(String... strings) {
        String rc = "";
        connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Log.e("JBDC Driver", " driver is ready");

            //неудаленный сервер
            //String url = "jdbc:jtds:sqlserver://192.168.100.3;databaseName=LR17;user=UserLR17;password=user";

            //удаленный сервер (без данных для ввода :) )
            String url = "jdbc:jtds:sqlserver://;databaseName=;user=;password=";

            connection = DriverManager.getConnection(url);
            Log.e("SQL Server", "connected successfully");

        } catch (ClassNotFoundException e) {
            Log.e("JBDC Driver", " driver isn't ready");
        } catch (SQLException throwables) {
            Log.e("SQL Server", "connected failed");
        }

        return null;
    }

    String Query()
    {
        //для вып-я запросов
        String result = "";
        String query = "select * from Login";
        try {
            //для взаимодействия с бд приложение отправляет команду серверу
            Statement statement = connection.createStatement();
            //отправление серверу запрос на выпоонение
            ResultSet resultSet = statement.executeQuery(query);
            //перемещ к след строке, делая ее текущей

            while (resultSet.next())
            {
                result += resultSet.getString("name") + "\n";
                Log.e("Query", "success");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Log.e("Query", "fail");
        }
        return result;
    }

    String QueryParam()
    {
        String result = "";
        String query = "select name from Login where name like '%i%'";
        try {
            //для взаимодействия с бд приложение отправляет команду серверу
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next())
            {
                result += resultSet.getString("name") + "\n";
                Log.e("ParamQuery", "success");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Log.e("ParamQuery", "fail");
        }
        return result;
    }

    String Procedure()
    {
        String result = "";
        CallableStatement callableStatement = null;
        try {
            callableStatement = connection.prepareCall("dbo.ProcedureButton");
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next())
            {
                result += resultSet.getString("name") + "\n";
                Log.e("Procedure", "success");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Log.e("Procedure", "fail");
        }
        return result;
    }

    String Function()
    {
        String result = "";
        Statement Statement = null;
        String query = "select * from dbo.FunctionButton()";
        try {
            Statement = connection.createStatement();
            ResultSet resultSet = Statement.executeQuery(query);
            while (resultSet.next())
            {
                result += resultSet.getString("name") + "\n";
                Log.e("Function", "success");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Log.e("Function", "fail");
        }
        return result;
    }

    String Batch()
    {
        //для вып-я запросов
        Statement Statement = null;
        String result = "";
        String query = "select name from Login where id = 1 or id = 11";

        //наследует от стэйтмент
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("set IDENTITY_INSERT Login ON " + "\ninsert into Login(id, name) values(?,?)");
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2,"one");
            preparedStatement.addBatch();

            preparedStatement.setInt(1, 11);
            preparedStatement.setString(2,"eleven");
            preparedStatement.addBatch();

            preparedStatement.executeBatch();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            Statement = connection.createStatement();
            ResultSet resultSet = Statement.executeQuery(query);
            while (resultSet.next())
            {
                result += resultSet.getString("name") + "\n";
                Log.e("Batch", "success");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Log.e("Batch", "fail");
        }
        return result;
    }

    String Update()
    {
        String result = "";
        String query = "update Login set name = 'UpdateButton' where id = '1' select name from Login where id = '1'";
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery(query);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next())
            {
                result += resultSet.getString("name") + "\n";
                Log.e("Update", "success");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Log.e("Update", "fail");
        }
        return result;
    }

    String Delete()
    {
        String result = "";
        String query = "delete from Login where id = '1' select name from Login";
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery(query);
            ResultSet resultSet = statement.executeQuery(query);

            //перемещ к след строке, делая ее текущей
            while (resultSet.next())
            {
                result += resultSet.getString("name") + "\n";
                Log.e("Delete", "success");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Log.e("Delete", "fail");
        }
        return result;
    }
}
