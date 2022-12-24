package com.psmsdb.lr16;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mName, mOrg, mPhone, mWork;
    private ListView mListContact;

    private ArrayList<Contact> contacts;
    private ListAdapter listAdapter;

    private String selectedContactId = null;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contacts = new ArrayList<>();
        mListContact = findViewById(R.id.list);

        mName = findViewById(R.id.name);
        mPhone = findViewById(R.id.phone);
        mWork = findViewById(R.id.work);

        findViewById(R.id.button_add).setOnClickListener(this);
        findViewById(R.id.button_delete).setOnClickListener(this);

        loadContacts();

        //нажатие на объект списка
        mListContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object selected = mListContact.getItemAtPosition(position);
                Contact contact = (Contact) selected;
                selectedContactId = contact.getID();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we can't display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void loadContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        else
        {
            String phoneNumber, workNumber, phoneID;

            //взаимодействие с провайдером контента
            ContentResolver contentResolver = getContentResolver();

            //query нужен для получения данных (предоставляет метод квери провайдера)
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            //столбцы, которые будут получены в результате запроса
            //помещение условия для выбора определенных строк (where)
            //условие фильтра строк

            if (cursor != null) {
                //перебираем и получаем отдельные данные
                while (cursor.moveToNext()) {
                    Contact contact = new Contact();

                    //getcolumnindex - возвращает индекс столбца
                    //getstring - значение
                    //конкретные объекты
                    String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                    if (hasPhoneNumber > 0) {
                        contact.setName(contactName);

                        Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null);

                        while (phoneCursor.moveToNext()) {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //workNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneID = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                            contact.setPhoneNumber(phoneNumber);
                            //contact.setWorkNumber(workNumber);
                            contact.setID(phoneID);
                        }
                        phoneCursor.close();
                    }
                    contacts.add(contact);
                }
            }

            listAdapter = new ListAdapter(MainActivity.this, R.layout.item, contacts);
            mListContact.setAdapter(listAdapter);
        }
    }

    protected void add() {

        String phone = mPhone.getText().toString();
        String workphone = mWork.getText().toString();
        String name = mName.getText().toString();
        ArrayList <ContentProviderOperation> ops = new ArrayList<>();

        //добавляем пустой контакт
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //добавляем данные имени
        if (name != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                    .build());
        }

        //организация
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, "Organization")
                    .build());

        //рабочий номер
        if (workphone != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, workphone)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }

        //добавляем данные номера
        if (phone != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        updateListContacts();
    }

    protected void delete() {
        //если контакт выбран - выполнится код
        if(selectedContactId != null) {
            final ArrayList ops = new ArrayList();
            final ContentResolver cr = getContentResolver();
            ops.add(ContentProviderOperation
                    .newDelete(ContactsContract.RawContacts.CONTENT_URI)
                    .withSelection(ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{selectedContactId})
                    .build());

            try {
                cr.applyBatch(ContactsContract.AUTHORITY, ops);
                ops.clear();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else{
            Toast.makeText(MainActivity.this, "Выберите контакт", Toast.LENGTH_SHORT).show();
        }
        updateListContacts();
    }

    protected void updateListContacts() {
        mName.setText(""); mPhone.setText(""); mWork.setText("");
        contacts.clear();
        loadContacts();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add:
                add();
                break;
            case R.id.button_delete:
                delete();
                break;
        }
    }
}