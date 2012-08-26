package se.knowit.android.vw.servicecenter;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.TextView;

public class MobGarantiActivity extends Activity {
    private static final String TAG = "MobGarantiActivity";
    
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.mobgaranti);
        
        String s = "<b>Vad är Volkswagen Mobilitetsgaranti?</b><br/>Mobilitetsgarantin innebär att du får hjälp på plats om din Volkswagen skulle bli akut okörbar, dygnet runt, 365 dagar om året. Det spelar ingen roll var du befinner dig – ring så får du all den hjälp du behöver. MobilitetsGarantin gör att du aldrig blir stående och gäller alla Volkswagenmodeller från 1986 och framåt, förutsatt att du lämnar in din bil för en Grundservice/Long Life Service. Garantin ingår också i Volkswagen Serviceavtal.<br/><br/><b>Om oturen skulle vara framme</b><br/>När du behöver hjälp med din Volkswagen ringer du <a href='tel:020-66 55 00'>020-66 55 00</a> inom Sverige eller <a href='tel:+46 771 66 55 00'>+46 771 66 55 00</a> från övriga Europa för att få assistans dygnet runt. Om möjligt så får du hjälp direkt på platsen, t ex med kilremmar, säkringar och slangklämmor.<br/><br/>Vill du veta mer om Volkswagen Mobilitetsgaranti, kontakta din Volkswagenhandlare eller gå in på <a href='www.volkswagen.se'>www.volkswagen.se</a><br/>";
	    if ("transport".equals(System.getProperty("background"))) {
	        s = "<b>Volkswagen Transportbilar</b><br/>CarePort Mobilitetsgaranti i korta ordalag<br/><br/>" +
	        		"- 3 års garanti, utan milbegränsning.<br/>" +
	        		"- MaxiMil driftgaranti (upp till 5 år) med stilleståndsersättning på 5% av gällande prisbasbelopp (avrundat uppåt till närmaste 100-tals kronor) fn. 2 200 kr exkl. moms per påbörjat dygn (gäller juridisk person samt fysisk person som bedriver näringsverksamhet).<br/>" +
	        		"- MobilitetsGaranti med bärgning, lånebil eller fri övernattning på hotell.<br/>" +
	        		"- 12 års garanti mot genomrostning för: Transporter, Caravelle, Multivan från årsmodell 2004 (typ 7H/7J), Caddy från årsmodell 2004 (typ 2K), Crafter (typ 2E/2F)<br/>" +
	        		"- 8 års garanti mot genomrostning för: Transporter, Caravelle och Multivan, typ 7D årsmodell 1999-2003<br/>" +
	        		"- 6 års garanti mot genomrostning för: Amarok<br/><br/>" +
	        		"<b>Om oturen skulle vara framme</b><br/>" +
	        		"När du behöver hjälp med din Volkswagen Transportbil ringer du:<br/>" +
	        		"För konsument: <a href='tel:020-66 55 00'>020-66 55 00</a>, <a href='tel:+46 771-66 55 00'>+46 771-66 55 00</a> från övriga Europa.<br/>" +
	        		"För juridisk person samt fysisk person som bedriver näringsverksamhet: <a href='tel:020-30 30 00'>020-30 30 00</a> inom Sverige, <a href='tel:+46 771-10 10 00'>+46 771-10 10 00</a> från övriga Europa.<br/><br/>" +
	        		"För mer information, kontakta din Volkswagen Transportbilar-handlare.<br/>";
	    }
	    
		TextView routeDescription = (TextView) findViewById(R.id.mobgaranti_text);
		routeDescription.setText(Html.fromHtml(s));
		Linkify.addLinks(routeDescription, Linkify.ALL);
	}
}
