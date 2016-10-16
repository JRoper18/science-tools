package Chemistry;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ulysses Howard Smith on 9/26/2016.
 */
public class AtomicElement {
    public final String name;
    public final String elementNames;
    public final String molecularFormula;
    public AtomicElement(String name){
        this.name = name;
        this.elementNames = name;
        this.molecularFormula = this.getElementData(name, "MolecularFormula");
    }
    private String getElementData(String name, String property){
        //PubChem Rest API Spec: https://pubchem.ncbi.nlm.nih.gov/pug_rest/PUG_REST_Tutorial.html#_Toc458584414
        //More here: https://pubchem.ncbi.nlm.nih.gov/pug_rest/PUG_REST.html#_Toc458584223
        String urlStr =  "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/name/\" + name + \"/property/\" + property + \"/JSON";
        try{
            URL url = new URL(urlStr);
            StringBuilder chemString = new StringBuilder();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream())
            );
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                chemString.append(inputLine);
            in.close();
            JSONObject chemData = new JSONObject(chemString.toString());
            String wantedData = chemData.getJSONObject("PropertyTable").getJSONArray("Properties").getJSONObject(0).getString(property);
            return wantedData;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return "DNE";
    }
}
