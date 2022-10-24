package eti.italiviocorrea.myxsdparser;

import org.apache.ws.commons.schema.*;

import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static XmlSchemaCollection xmlSchemaCollection;
    private static Map<QName, List<XmlSchemaElement>> xsdElements = new HashMap<QName, List<XmlSchemaElement>>();

    private static Map<QName, List<XmlSchemaAttribute>> xsdAttributs = new HashMap<QName, List<XmlSchemaAttribute>>();
    private static List<XmlSchemaElement> schemaElements = new ArrayList<XmlSchemaElement>();


    public static void main(String[] args) throws URISyntaxException, FileNotFoundException, UnsupportedEncodingException {
        // Path for the file which is stored within the Resource folder
        String xsdPath = Main.class.getClassLoader().getResource("").getPath();
        System.out.println(xsdPath);
        String filePath = Path.of(xsdPath + "/cte3/3.00/enviCTe_v3.00.xsd").toString();

//        InputStream is = Main.class.getResourceAsStream("/cte3/cte_v3.00.xsd");;
        InputStream is = new FileInputStream(filePath);

        xmlSchemaCollection = new XmlSchemaCollection();
        xmlSchemaCollection.setBaseUri(xsdPath + "/cte3/3.00");
        // Schema contain the complete XSD content which needs to be parsed
        XmlSchema schema = xmlSchemaCollection.read(new StreamSource(is));

        schema.write(System.out);

        // Get the root element from XSD
        Map.Entry<QName, XmlSchemaElement> entry = schema.getElements().entrySet().iterator().next();
        QName rootElement = entry.getKey();

        // Get all the elements based on the parent element
        XmlSchemaElement childElement = xmlSchemaCollection.getElementByQName(rootElement);

        // Call method to get all the child elements
        getChildElementNames(childElement);

        String element = "Elements : " + xsdElements.entrySet().stream().map(e -> e.getKey() + " -- " + String.join(", ", e.getValue().stream().filter(xmlSchemaElement -> !schemaElements.isEmpty()).map(v -> v
                .getName()).collect(Collectors.toList()))).collect(Collectors.toList());

        System.out.println(element);

        String atrib = "Attributes : " + xsdAttributs.entrySet().stream().map(e -> e.getKey() + " -- " + String.join(", ", e.getValue().stream().filter(xmlSchemaElement -> !xsdAttributs.isEmpty()).map(v -> v
                .getName()).collect(Collectors.toList()))).collect(Collectors.toList());

        System.out.println(atrib);
    }

    // Method to check for the child elements and return list
    private static void getChildElementNames(XmlSchemaElement element) {


        // Get the type of the element
        XmlSchemaType elementType = element != null ? element.getSchemaType() : null;

        // Confirm if the element is of Complex type
        if (elementType instanceof XmlSchemaComplexType) {

            // pega os atributos do elemento
            List<XmlSchemaAttributeOrGroupRef> atribs = ((XmlSchemaComplexType)elementType).getAttributes();
            if(!atribs.isEmpty()){
                atribs.stream().forEach(xmlSchemaAttributeOrGroupRef -> {
                    if(xmlSchemaAttributeOrGroupRef instanceof XmlSchemaAttribute) {
                        System.out.println("Attribute Schema Type");
                        XmlSchemaAttribute itemAttrib = (XmlSchemaAttribute) xmlSchemaAttributeOrGroupRef;
                        addAttribute(element.getQName(),itemAttrib);
                    }
                });
            }

            // Pega todos particles associado com o elemento
            XmlSchemaParticle allParticles = ((XmlSchemaComplexType) elementType).getParticle();

            // Check particle belongs to which type
            if (allParticles instanceof XmlSchemaAny) {
                System.out.println("Any Schema Type");

            } else if (allParticles instanceof XmlSchemaElement) {
                System.out.println("Element Schema Type");

            } else if (allParticles instanceof XmlSchemaChoice) {
                System.out.println("Choice Schema Type");

            } else if (allParticles instanceof XmlSchemaSequence) {
                System.out.println("Sequence Schema Type");
                final XmlSchemaSequence xmlSchemaSequence = (XmlSchemaSequence) allParticles;
                final List<XmlSchemaSequenceMember> items = xmlSchemaSequence.getItems();
                items.forEach((item) -> {
                    if (item instanceof XmlSchemaElement) {
                        XmlSchemaElement itemElements = (XmlSchemaElement) item;
                        schemaElements.add(itemElements);
                        //Chama o método addChild para adicionar os filhos do corrente elemento
                        addChild(element.getQName(), itemElements);
                        // Call method recursively to get all subsequent element
                        getChildElementNames(itemElements);
                        //schemaElements = new ArrayList<XmlSchemaElement>();
                    } else if (item instanceof XmlSchemaChoice) {
                        XmlSchemaChoice itemChoice = (XmlSchemaChoice) item;
                        itemChoice.getItems().stream().forEach(xmlSchemaChoiceMember -> {
                            if (xmlSchemaChoiceMember instanceof XmlSchemaElement) {
                                XmlSchemaElement itemElements = (XmlSchemaElement) xmlSchemaChoiceMember;
                                schemaElements.add(itemElements);
                                //Call the method to add the current element as child
                                addChild(element.getQName(), itemElements);
                                // Call method recursively to get all subsequent element
                                getChildElementNames(itemElements);
                                //schemaElements = new ArrayList<XmlSchemaElement>();

                            }
                        });
                    }
                });

            } else if (allParticles instanceof XmlSchemaGroupRef) {

            }else {
                System.out.println("Desconhecido Schema Type");
            }
        }
    }

    // Add child elements based on its parent
    public static void addChild(QName qName, XmlSchemaElement child) {
        List<XmlSchemaElement> values = xsdElements.get(qName);
        if (values == null) {
            values = new ArrayList<XmlSchemaElement>();
        }
        values.add(child);
        xsdElements.put(qName, values);
    }

    public static void addAttribute(QName qName, XmlSchemaAttribute child) {
        List<XmlSchemaAttribute> values = xsdAttributs.get(qName);
        if (values == null) {
            values = new ArrayList<XmlSchemaAttribute>();
        }
        values.add(child);
        xsdAttributs.put(qName, values);
    }

}
