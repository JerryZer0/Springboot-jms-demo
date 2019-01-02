package com.example.demo.broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Howells
 * @ClassName TestSender.java
 * @Description TODO
 * @createTime 01/02/2019
 */

@Component
public class TestSender  implements ApplicationRunner {
    @Autowired
    private JmsTemplate template;

    @Override
    public void run(ApplicationArguments args) {
        try {
            for (int i = 0; i <1 ; i++) {
                String msg = "{\"eventInfo\":{\"eventCode\":\"BR\"},\"shipment\":{\"party\":[{\"partyId\":1,\"companyId\":\"SMARUS772643806\",\"partyName\":\"TEST COMPANY\",\"partyType\":\"CGN\"}],\"numberAssociation\":[{\"csBookingReferenceNumber\":\"BR001\",\"bookingNumber\":\"BC004\"}],\"container\":[{\"containerId\":\"1\",\"containerNumber\":\"OOCL111111\",\"checkDigital\":\"1\",\"sizeType\":\"45GC\",\"grossWeight\":\"20KGS\",\"inboundHaulage\":\"M\",\"outboundHaulage\":\"M\",\"inboundTrafficeMode\":\"Rail\",\"outboundTrafficeMode\":\"Truck\",\"isSoc\":\"0\",\"refeerInfo\":{\"atmosphereType\":\"NP\",\"temperature\":\"1\",\"temperatureUnit\":\"C\"}},{\"containerId\":\"2\",\"containerNumber\":\"OOCL222222\",\"checkDigital\":\"2\"}],\"extenalReference\":[{\"referenceId\":1,\"referenceType\":\"PO\",\"referenceNumber\":\"PO111111\"}],\"cargo\":[{\"cargoId\":1,\"cargoDescription\":\"iMac mini\",\"grossWeight\":4500,\"grossWeightUnit\":\"KG\",\"netWeight\":800,\"netWeightUnit\":\"KG\"}],\"routes\":[{\"point\":[{\"locationType\":\"MTP\",\"locationName\":\"\",\"arrivalEstimateTime\":{}},{\"locationType\":\"ORG\",\"locationName\":\"\",\"arrivalEstimateTime\":{}},{\"locationType\":\"POL\",\"locationName\":\"HamburgHamburgHamburgDEHAMGermany\",\"arrivalService\":\"\",\"arrivalVesselName\":\"COSCO ADEN\",\"arrivalVoyageNumber\":\"822E\",\"arrivalDirection\":\"\",\"arrivalEstimateTime\":{},\"departureService\":\"\",\"departureVesselName\":\"COSCO ADEN\",\"departureVoyageNumber\":\"822E\",\"departureDirection\":\"\",\"departureEstimateTime\":{}},{\"locationType\":\"POD\",\"locationName\":\"HamburgHamburgHamburgDEHAMGermany\",\"arrivalService\":\"\",\"arrivalVesselName\":\"COSCO ADEN\",\"arrivalVoyageNumber\":\"822E\",\"arrivalDirection\":\"\",\"arrivalEstimateTime\":{},\"departureService\":\"\",\"departureVesselName\":\"COSCO ADEN\",\"departureVoyageNumber\":\"822E\",\"departureDirection\":\"\",\"departureEstimateTime\":{}},{\"locationType\":\"FND\",\"locationName\":\"\",\"arrivalEstimateTime\":{}}]}]}}";

                System.out.println("successful");
                template.setPubSubDomain(false);
                template.convertAndSend("Queue", msg);
//            String json = "{\"eventInfo\":{\"eventCode\":\"BR\"},\"shipment\":{\"party\":[{\"partyId\":1,\"companyId\":\"SMARUS772643806\",\"partyName\":\"TEST COMPANY\",\"partyType\":\"CGN\"}],\"numberAssociation\":[{\"csBookingReferenceNumber\":\"BR001\",\"bookingNumber\":\"BC004\"}],\"container\":[{\"containerId\":\"1\",\"containerNumber\":\"OOCL111111\",\"checkDigital\":\"1\",\"sizeType\":\"45GC\",\"grossWeight\":\"20KGS\",\"inboundHaulage\":\"M\",\"outboundHaulage\":\"M\",\"inboundTrafficeMode\":\"Rail\",\"outboundTrafficeMode\":\"Truck\",\"isSoc\":\"0\",\"refeerInfo\":{\"atmosphereType\":\"NP\",\"temperature\":\"1\",\"temperatureUnit\":\"C\"}},{\"containerId\":\"2\",\"containerNumber\":\"OOCL222222\",\"checkDigital\":\"2\"}],\"extenalReference\":[{\"referenceId\":1,\"referenceType\":\"PO\",\"referenceNumber\":\"PO111111\"}],\"cargo\":[{\"cargoId\":1,\"cargoDescription\":\"iMac mini\",\"grossWeight\":4500,\"grossWeightUnit\":\"KG\",\"netWeight\":800,\"netWeightUnit\":\"KG\"}],\"routes\":[{\"point\":[{\"locationType\":\"MTP\",\"locationName\":\"\",\"arrivalEstimateTime\":{}},{\"locationType\":\"ORG\",\"locationName\":\"\",\"arrivalEstimateTime\":{}},{\"locationType\":\"POL\",\"locationName\":\"HamburgHamburgHamburgDEHAMGermany\",\"arrivalService\":\"\",\"arrivalVesselName\":\"COSCO ADEN\",\"arrivalVoyageNumber\":\"822E\",\"arrivalDirection\":\"\",\"arrivalEstimateTime\":{},\"departureService\":\"\",\"departureVesselName\":\"COSCO ADEN\",\"departureVoyageNumber\":\"822E\",\"departureDirection\":\"\",\"departureEstimateTime\":{}},{\"locationType\":\"POD\",\"locationName\":\"HamburgHamburgHamburgDEHAMGermany\",\"arrivalService\":\"\",\"arrivalVesselName\":\"COSCO ADEN\",\"arrivalVoyageNumber\":\"822E\",\"arrivalDirection\":\"\",\"arrivalEstimateTime\":{},\"departureService\":\"\",\"departureVesselName\":\"COSCO ADEN\",\"departureVoyageNumber\":\"822E\",\"departureDirection\":\"\",\"departureEstimateTime\":{}},{\"locationType\":\"FND\",\"locationName\":\"\",\"arrivalEstimateTime\":{}}]}]}}";
//            ShipmentMessage shipmentMessage = JsonUtils.jsonToObject(json, ShipmentMessage.class);
//            shipmentMessageService.process(shipmentMessage);
            }
        } catch (Exception e) {
        }
    }
}
