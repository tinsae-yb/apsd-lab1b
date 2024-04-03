package org.example;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PensionPlanAdapter implements JsonSerializer<PensionPlan>, JsonDeserializer<PensionPlan> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public JsonElement serialize(PensionPlan src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("planReferenceNumber", src.getPlanReferenceNumber());
        jsonObject.addProperty("enrollmentDate", formatter.format(src.getEnrollmentDate()));
        jsonObject.addProperty("monthlyContribution", src.getMonthlyContribution());
        return jsonObject;
    }

    @Override
    public PensionPlan deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        String planReferenceNumber = jsonObject.get("planReferenceNumber").getAsString();
        LocalDate enrollmentDate = LocalDate.parse(jsonObject.get("enrollmentDate").getAsString(), formatter);
        double monthlyContribution = jsonObject.get("monthlyContribution").getAsDouble();


        PensionPlan pensionPlan = new PensionPlan();
        pensionPlan.setPlanReferenceNumber(planReferenceNumber);
        pensionPlan.setEnrollmentDate(enrollmentDate);
        pensionPlan.setMonthlyContribution(monthlyContribution);
        return pensionPlan;
    }
}
