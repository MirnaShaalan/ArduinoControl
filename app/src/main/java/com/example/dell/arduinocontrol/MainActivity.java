package com.example.dell.arduinocontrol;
// دي باكديج الابليكيشن اللي عايزين نعمله

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

 // دول مجموعه من الكلاسس بتكرن متعرفه اصلا جوا البرنامج بتحتوي ع وظائف وخصائص معينه بنعمل import لاي كلاس معين احنا محتاجينه عن طريق اننا نكتبها زي ماهي موجوده كدا او نعملها ctrl+enter زي ماهنشوف تحت


public class MainActivity extends AppCompatActivity {

    // عشان نبدأ نعمل اب معين بنفكر الاول ف الشكل الخارجي يعني هيكون فيه مثلا كم button وكم textview وايه وظايفهم وهكذا
    // هنختار طريقه  اننا نكون بيها ال user interface للابلكيشين بتاعنا وليكن هنستخدم الطريقة الاسهل وهي ال drag and drop
    // بعد ماظبطنا شكل ال layout زي مااحنا محتاجين هنبدأ نسمي كل widget اللي هي مثلا ال buttons  وال textviews باسماء معينه عشان نقدر نتعامل معاهم بعد كدا ونبرمجهم
    // الاسم دا هو ال id


    // هنبدأ نعرف ال objects عشان نقدر نتعامل بيها ف ال coding  زي ماكنا بنعمل ف اي لغة برمجه قبل كدا

    TextView txt;
    // التكست فيو دا وظيفته انه يعرض تكست احنا بنحدده من خلال الكود عن طريق دالة SetText
    Button on ;
    Button off;
    // دي طبعا ال لbuttons هنضغط عليها عشان ننفذ امر معين هنححده بعدين


    BluetoothAdapter btab;

    // ال bluetoothadapter دا كلاس من خلال تعريف اوبجكت منه هنقدر ن access البلوتوث ف الموبايل ..وظيفته باختصار اننا نعرف اذا كان فيه بلوتوث ف الجهاز بتاعنا او لا ولو فيه نقدر نعرف حالته مقفول او مفتوح ونفتحه
    BluetoothDevice device;
    // ال bluetoothdevice هو المسئول عن التواصل مع أجهزة البلوتوث المقترنه ..ف المثال بتاعنا البلوتوث موديول مثلا.. نقدر نعرف من خلاله معلومات خاصه بالجهاز دا زي اسمه وال adress بتاعه
    BluetoothSocket socket;
    // ببساطه جدا نقدر نقول ان دا طريق هيتفتح بين ال client و ال server اللي هما الموبايل بتاعنا و البلوتوث موديول اللي هتأهلنا بعد كدا اننا نرسل ونستقبل داتا
    InputStream inputstr;
    // وظيفته هنا اننا نقدر نبعت من الموبايل للموديول
    OutputStream outputstr;
    // نستقبل من الموديول للموبايل والاتنين بيتعاملو مع الداتا بالنظام ال binary


    byte[] buffer;
    int position;
    boolean stop;
    // الحاجات دي خليها دلوقت هنعرفها تحت

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // دا جزء ثابت اول مابنفتح البرنامج بيكون موجود ودا بيغرف ال layout بتاعتنا .. والactivity_main دا ال id لل layout



        Button openbt = (Button) findViewById(R.id.open);
        // هنا بنعرف اوبجكت من كلاس الbutton ونربطه بال id اللي قلنا عليه قبل كدا
        // ال id هنا هو ال open واسم الاوبجكت بتاعنا دا احنا اللي بنختاره اللي هو هنا openbt ودا اللي هنستخدمه بعد كدا عشان نتعامل بيه يعني مش هنحتاج ال id تاني
        // ملحوظه : فيه طريقتين للتعريف ممكن نعرف الاوبجكت لوحده زي مامعملنا فوق ف on و off وبعدين نرجع نربطهم بال id او نعرف ونربط بال id ف نفس السطر زي ماعملنا هنا دلوقت
        final Button paired = (Button) findViewById(R.id.paired);
        // المفروض لو كتبت سطر الكود دا كدا من غير ماعمل import للكلاس بتاعه هيديني error ..هقف ع السطر واعمل crtl+enter هيعمل import للكلاس المطلوب وهكذا مع كل الحاجات اللي هنحتاج نعملها import
        paired.setVisibility(View.GONE);
        // دا امر بيخفي ال button من ال layout بعد كدا هنعمل امر يظهره وقت مانحتاج

        Button connect = (Button) findViewById(R.id.connect);
        Button close = (Button) findViewById(R.id.close);
         on = (Button) findViewById(R.id.on);

         off = (Button) findViewById(R.id.off);

         txt = (TextView) findViewById(R.id.txt);




        openbt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // هنا هضيف الاوامر اللي عايز انفذها لما اضغط ع الbutton

                findblue();
                // دي داله انا معرفاها تحت فيها اوامر معينه هنشوفها كمان شويه

                 paired.setVisibility(View.VISIBLE);

                // دا أمر بيظهر ال button اللي انا كنت خافياه قبل كدا
              // يعني باختصار اول ماافتح الاب الbutton اللي اسمه paired مش هيكون ظاهر ..اول مااضغط علي الbutton اللي اسمه openbt هيظهر واقدر اتعامل معاه



            }


        });
        // هنا معناها اني لما اضغط علي ال button هيحصل حدث معين انا اللي بححده .. يعني اي حاجه بحطها بين الاقواس دي مش هتتنفذ الا لما اغضط ع ال button
        // ف حالة ال button اللي فوق دا هينفذ حاجتين .. الداله اللي اسمها findblue وهيظهر ال paired button


        paired.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                 Intent mIntent = new Intent(MainActivity.this, Main2Activity.class);
                // ال intent دا حاجه بتستخدم عشان اتنقل بين محتويات الابليكيشن .. هنا فيه اتنين arguments  الاول دا اسم ال activity اللي انا موجود فيها والتاني دا اسم ال activity اللي عايزة اروحلها
                 startActivity(mIntent);

                // دا معناه ابدأ تنفيذ ال intent
              // الموضوع دا كلو معناه اني لما اضغط ع ال button دا هيتنقل ل layout تانيه اللي هي المفروض هيكون فيها الاجهزة المقترنه عشان اختار منهم واحد..والكلام دا له كوود معين هيكون ف كلاس تاني بكود تاني هنعرفه كمان شويه


            }


        });



        connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MainActivity.this, "YOU ARE CONNECTED..",
                        Toast.LENGTH_SHORT).show();
                // ال toast دي الرساله اللي بتكون ف مستطيل اسمر صغير بتظهر ف اخر شاشة الموب وتختفي بعد فتره
                // اول argument بيحررر ال activity اللي هيظهر فيها ال toast  تاني حاجه دا النص اللي عايزة يظهر تالت حاجه دا المده اللي هيظهر فيها ويختفي






            }


        });


        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MainActivity.this, "THE CONNECTION HAS BEEN CANCELLED..",
                        Toast.LENGTH_SHORT).show();


            }


        });


        on.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    onbt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "THE FAN IS ON..",
                        Toast.LENGTH_SHORT).show();


            }


        });


        off.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    offbt();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MainActivity.this, "THE FAN IS OFF..",
                        Toast.LENGTH_SHORT).show();


            }




        });








    }

    // نيجي هنا للدوال اللي احنا استدعيناها فوق هنشوف كل داله فيهم بتعمل ايه

     void findblue() {
         // الداله دي مسئوله عن البلوتوث بتشوف هو مفتووح اصلا او مقفول .. ولو مقفول بتفتحه
         btab = BluetoothAdapter.getDefaultAdapter();
        if (btab == null) {
            txt.setText("No bluetooth adapter available");
            // لو الجهاز بتاعي مبيدعمش البلوتوث هيطلعلي تكست ع الtextview اللي عملناه فيه الكلام الل جوا القوسين دا
        }
        if (!btab.isEnabled()) {
            // لو البلوتوث مقفول افتحه
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBluetooth);
            // امر فتح البلوتوث
        }
        if(btab.isEnabled()){
            // لو البلوتوث مفتوح اصلا هيطلعلي toast تقول انه مفتوح اصلا
            Toast.makeText(MainActivity.this, "BLUETOOTH IS ON..",
                    Toast.LENGTH_SHORT).show();


        }
    }

    void connect()throws IOException
    {

        BluetoothAdapter myBluetooth = BluetoothAdapter.getDefaultAdapter();
        device = myBluetooth.getRemoteDevice(getIntent().getStringExtra("adress"));
        // 20:15:05:07:10:51
        // ال الكلام اللي جوا القوس دا getIntent().getStringExtra("EXTRA_ADDRESS" معناه اني باخد ال adress بتاع الجهاز من ال activity التانيه عشان استخدمه ابني بيه ال connection هنا


        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        socket = device.createRfcommSocketToServiceRecord(uuid);
        // الموضوع دا عايز سيرش عشان نفهم ميكانزم ال connection بالبلوتوث ..بس نقدر نقول ببساطه ان السطرين دول ثابتين عشان افتح طريق للاتصال بالبلوتوث
        socket.connect();
        // هنا بقوله ابدأ الاتصال
        outputstr = socket.getOutputStream();
        // بفتح عن طريق ال socket طريق لارسال البيانات
        inputstr = socket.getInputStream();
        // بفتح طريق لاستقبال الداتا
        recievedata();
         // داله احنا معرفينها بتعمل وظيفه معينه

    }


    void recievedata()
    {

        final byte delimiter = 10; //ASCII code for a newline character
        //newLine=Enter
        stop = false;
        position = 0;
        buffer = new byte[1024];
        final Handler handler = new Handler();
        // ال handler دا المسئول عن اي تعديل بيحدث من ال thread  ع ال ui
       // ال thread دا حاجه بتسمح انو ينفذ الكود الموجود جواه علي التوازي مع باقي الكود اللي موجود معانا من غير ماحد يعطل التاني.. يعني هيخلي الاب يبعت ويستقبل ف نس الوقت
       Thread m = new Thread(new Runnable()

               // ال thread دا باختصار اللي بيسمح ان يكون فيه حاجتين شغالين مع بعض ف نفس الوقت يعني هكمل ف الاب بتاعي عادي واللي جوا ال thread برضو شغال ف نفس الوقت
        {
            public void run()
            {


                while(!Thread.currentThread().isInterrupted() && !stop)
                    // طول مال thread شغال وstop= true يعني مدتلوش امر انو يفصل
                {
                    try
                    {
                        // الكود اللي تحت دا معناه باختصار اني هخزن الداتا اللي جايلي ف array وبختبر كل عنصر لوحده وبنشوف اذا كان العنصر دا بيساوي delimiter ولا لا
                        // لو بيساوي معني كدا ان المستخدم ضغط enter وخلص الداتا اللي عايز يبعتها فهبدأ انسخ ال array ل array تانيه واحولها ل string عشان اعرضها ك تكست واصفر العداد عشان ابدأ استقبل داتا تانيه وهكذا
                        // لو مبتساويش ال delimiter يبقي هستمر ف استقبال الداتا ف نفس ال array
                        int bytesAvailable = inputstr.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            inputstr.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                    // بختبر اذا الداتا بتعتي خلصت ابدأ ف عرضها ولا لسه بيستقبل
                                {
                                    byte[] encodedBytes = new byte[position];
                                    System.arraycopy(buffer, 0, encodedBytes, 0, encodedBytes.length);
                                    // بنسخ ال array اللي فيها الداتا ف array اخري عشان نتعامل معاها
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    // بحول ال array ل string
                                    position = 0;
                                    // بصفر العداد يعني بقوله احنا كدا خلصنا اول سطر من الداتا ابدأ من اول ال array تاني عشان نستقبل سطر جديد ونعرضه
                                    handler.post(new Runnable()

                                    {
                                        public void run()
                                        {
                                            // الامر المئول عن تعديل ف ال ui عن طريق  ال handler
                                           txt.setText(data);
                                            //امر عرض الداتا اللي استقبلناها ع ال textview


                                        }
                                    });
                                }
                                else
                                {
                                    buffer[position++] = b;
                                    // هنا بقوله زود العداد واحد عشان يكمل ع نفس ال array
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stop = true;
                    }
                }
            }
        });
        m.start();
        // ابدأ شغل ال thread اللي سميناه m
    }

    void onbt() throws IOException
    {
        outputstr.write("1".getBytes());
        // هنا بقوله الداله دي بتبعت واحد ولازم احوله ل binary عن طريق getBytes عشان زي ماقلنا outstream بتتعامل بال binary
    }
    void offbt() throws IOException
    {
        outputstr.write("0".getBytes());
        // نفس الموضوع
    }
    // احنا اخترنا اي رقمين ممكن نكتب اي ارقام تانيه بس لازن نستقبل نفس الارقام ب الاردوينو



    void close() throws IOException
    {
        stop = true;
        outputstr.close();
        inputstr.close();
        socket.close();

        // هنا بقفل كل الطرق وال connections اللي فتحتها

    }


}

