package com.pertamina.portal.iam.utils.PANHelper;

import com.pertamina.portal.core.utils.JsonXmlUtils;
import com.pertamina.portal.iam.models.PANModel.AddressModel;
import com.pertamina.portal.iam.models.PANModel.BPJSModel;
import com.pertamina.portal.iam.models.PANModel.BankModel;
import com.pertamina.portal.iam.models.PANModel.CommunicationModel;
import com.pertamina.portal.iam.models.PANModel.FamilyModel;
import com.pertamina.portal.iam.models.PANModel.FormalEducation;
import com.pertamina.portal.iam.models.PANModel.NPWPModel;
import com.pertamina.portal.iam.models.PANModel.PersonalData;
import com.pertamina.portal.iam.models.PANModel.PersonalIDModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;

public class PANDataUtils {

    public static PersonalData getPersonalData(Document dom) {
        PersonalData personalDataModel = new PersonalData();

        NodeList nPersonalData = dom.getElementsByTagName("PTM_MS_IT_PERSONAL_DATA");

        Document personalData = nPersonalData.item(0).getOwnerDocument();

        personalDataModel.setTitle(personalData.getElementsByTagName("PANREDM_TEXT").item(0).getTextContent());
        personalDataModel.setName(personalData.getElementsByTagName("PCNAMEM").item(0).getTextContent());
        personalDataModel.setLanguage(personalData.getElementsByTagName("PSPRSLM_TEXT").item(0).getTextContent());
        personalDataModel.setGender(personalData.getElementsByTagName("PGESCHM_TEXT").item(0).getTextContent());
        personalDataModel.setBirthDate(personalData.getElementsByTagName("PGBDATM").item(0).getTextContent());
        personalDataModel.setBirthPlace(personalData.getElementsByTagName("PGBORTM").item(0).getTextContent());
        personalDataModel.setCountry(personalData.getElementsByTagName("PGBLNDM_TEXT").item(0).getTextContent());
        personalDataModel.setNationality(personalData.getElementsByTagName("PNATIOM_TEXT").item(0).getTextContent());
        personalDataModel.setReligion(personalData.getElementsByTagName("PKONFEM_TEXT").item(0).getTextContent());
        personalDataModel.setStatus(personalData.getElementsByTagName("PFAMSTM_TEXT").item(0).getTextContent());
        personalDataModel.setSince(personalData.getElementsByTagName("PFAMDTM").item(0).getTextContent());
        personalDataModel.setTitlePTITELM_TEXT(personalData.getElementsByTagName("PTITELM_TEXT").item(0).getTextContent());
        personalDataModel.setSecondTitlePTITL2M_TEXT(personalData.getElementsByTagName("PTITL2M_TEXT").item(0).getTextContent());
        personalDataModel.setAdditionalTitle(personalData.getElementsByTagName("PNAMZUM_TEXT").item(0).getTextContent());

        return personalDataModel;
    }

    public static List<FamilyModel> getFamilyData(Document dom) {
        try {
            NodeList nFamily = dom.getElementsByTagName("PTM_MS_IT_FAMILY_MEMBER_DEPENDENTS");
            List<FamilyModel> familyModels = new ArrayList<>();

            for (int i = 0; i < nFamily.getLength(); i++) {
                JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nFamily.item(i));

                FamilyModel familyModel = new FamilyModel();

                familyModel.setStartDate(jo.getString("PBEGDAM"));
                familyModel.setEndDate(jo.getString("PENDDAM"));
                familyModel.setFamilyType(jo.getString("PSUBTYM_TEXT"));
                familyModel.setGender(jo.getString("PFASEXM_TEXT"));
                familyModel.setName(jo.getString("PFANAMM"));
                familyModel.setBirthPlace(jo.getString("PFGBOTM"));
                familyModel.setBirthDate(jo.getString("PFGBDTM"));
                familyModel.setMedicalReimb(jo.getString("PKDZUGM_TEXT"));
                familyModel.setAction(jo.getString("PRESE1M"));

                familyModels.add(familyModel);
            }

            return familyModels;
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<BankModel> getBankData(Document dom) {
        try {
            List<BankModel> bankModels = new ArrayList<>();
            NodeList nBank = dom.getElementsByTagName("PTM_MS_IT_BANK_DETAILS");

            for (int i = 0; i < nBank.getLength(); i++) {
                JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nBank.item(i));

                BankModel bankModel = new BankModel();

                bankModel.setStartDate(jo.getString("PBEGDAM"));
                bankModel.setEndDate(jo.getString("PENDDAM"));
                bankModel.setPayee(jo.getString("PEMFTXM"));
                bankModel.setPaymentMethod(jo.getString("PZLSCHM_TEXT"));
                bankModel.setBankCountry(jo.getString("PBANKSM_TEXT"));
                bankModel.setBankName(jo.getString("PBANKLM_TEXT"));
                bankModel.setAccountNumber(jo.getString("PBANKNM"));
                bankModel.setBankType(jo.getString("PSUBTYM_TEXT"));
                bankModel.setAction(jo.getString("PRESE1M"));

                bankModels.add(bankModel);
            }

            return bankModels;
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static BPJSModel getBPJSData(Document dom) {
        BPJSModel bpjsModel = new BPJSModel();

        NodeList nBPJS = dom.getElementsByTagName("PTM_MS_IT_JAMSOSTEK_INSURANCE");

        Document bpjs = nBPJS.item(0).getOwnerDocument();

        bpjsModel.setStartDate(bpjs.getElementsByTagName("PBEGDAM").item(0).getTextContent());
        bpjsModel.setEndDate(bpjs.getElementsByTagName("PENDDAM").item(0).getTextContent());
        bpjsModel.setJamsostekNumber(bpjs.getElementsByTagName("PJAMIDM").item(0).getTextContent());
        bpjsModel.setMarried(bpjs.getElementsByTagName("PMARSTM").item(0).getTextContent());
        bpjsModel.setAction(bpjs.getElementsByTagName("PRESE1M").item(0).getTextContent());


        return bpjsModel;
    }

    public static List<AddressModel> getAddressData(Document dom) {
        try {
            List<AddressModel> addressModels = new ArrayList<>();
            NodeList nAddress = dom.getElementsByTagName("PTM_MS_IT_ADDRESSES");
            for (int i = 0; i < nAddress.getLength(); i++) {
                JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nAddress.item(i));

                AddressModel addressModel = new AddressModel();

                addressModel.setStartDate(jo.getString("PBEGDAM"));
                addressModel.setEndDate(jo.getString("PENDDAM"));
                addressModel.setAddressType(jo.getString("PSUBTYM_TEXT"));
                addressModel.setContactName(jo.getString("PNAME2M"));
                addressModel.setStreetHouseNumber(jo.getString("PSTRASM"));
                addressModel.setAction(jo.getString("PRESE1M"));

                addressModels.add(addressModel);
            }

            return addressModels;
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<NPWPModel> getNPWPData(Document dom) {
        try {
            List<NPWPModel> npwpModels = new ArrayList<>();
            NodeList nNPWP = dom.getElementsByTagName("PTM_MS_IT_INDONESIAN_TAX_DATA");
            for (int i = 0; i < nNPWP.getLength(); i++) {
                JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nNPWP.item(i));

                NPWPModel npwpModel = new NPWPModel();

                npwpModel.setStartDate(jo.getString("PBEGDAM"));
                npwpModel.setEndDate(jo.getString("PENDDAM"));
                npwpModel.setNpwpNumber(jo.getString("PTAXIDM"));
                npwpModel.setStatusPajak(jo.getString("STATUS_PAJAK"));
                npwpModel.setSpouseBenefit(jo.getString("PSPBENM"));
                npwpModel.setAction(jo.getString("PRESE1M"));

                npwpModels.add(npwpModel);
            }

            return npwpModels;
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<CommunicationModel> getCommunicationData(Document dom) {
        try {
            List<CommunicationModel> communicationModels = new ArrayList<>();
            NodeList nCommunication = dom.getElementsByTagName("PTM_MS_IT_COMMUNICATION");

            for (int i = 0; i < nCommunication.getLength(); i++) {
                JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nCommunication.item(i));

                CommunicationModel communicationModel = new CommunicationModel();

                communicationModel.setStartDate(jo.getString("PBEGDAM"));
                communicationModel.setEndDate(jo.getString("PENDDAM"));
                communicationModel.setCommType(jo.getString("PSUBTYM_TEXT"));
                communicationModel.setValue(jo.getString("VALUE"));
                communicationModel.setAction(jo.getString("PRESE1M"));

                communicationModels.add(communicationModel);
            }

            return communicationModels;
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<PersonalIDModel> getPersonalIDData(Document dom) {
        try {
            List<PersonalIDModel> personalIDModels = new ArrayList<>();
            NodeList nPersonalID = dom.getElementsByTagName("PTM_MS_IT_PERSONAL_IDS");
            for (int i = 0; i < nPersonalID.getLength(); i++) {
                JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nPersonalID.item(i));

                PersonalIDModel personalIDModel = new PersonalIDModel();

                personalIDModel.setStartDate(jo.getString("PBEGDAM"));
                personalIDModel.setEndDate(jo.getString("PENDDAM"));
                personalIDModel.setIdType(jo.getString("PSUBTYM_TEXT"));
                personalIDModel.setIdNumber(jo.getString("PICNUMM"));
                personalIDModel.setAction(jo.getString("PRESE1M"));

                personalIDModels.add(personalIDModel);
            }

            return personalIDModels;
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<PersonalIDModel> getUniformData(Document dom) {
        try {
            List<PersonalIDModel> personalIDModels = new ArrayList<>();
            NodeList nPersonalID = dom.getElementsByTagName("PTM_MS_IT_PERSONAL_IDS");
            for (int i = 0; i < nPersonalID.getLength(); i++) {
                JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nPersonalID.item(i));

                PersonalIDModel personalIDModel = new PersonalIDModel();

                personalIDModel.setStartDate(jo.getString("PBEGDAM"));
                personalIDModel.setEndDate(jo.getString("PENDDAM"));
                personalIDModel.setUkuranBaju(jo.getString("PUK_BAJUM"));
                personalIDModel.setUkuranCelana(jo.getString("PUK_CELANAM"));
                personalIDModel.setUkuranSepatu(jo.getString("PUK_SEPATUM"));
                personalIDModel.setAction(jo.getString("PRESE1M"));

                personalIDModels.add(personalIDModel);
            }

            return personalIDModels;
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<FormalEducation> getFormalEducation(Document dom) {
        try {
            List<FormalEducation> formalEducations = new ArrayList<>();
            NodeList nFormalEducation = dom.getElementsByTagName("PTM_MS_IT_EDUCATION");
            for (int i = 0; i < nFormalEducation.getLength(); i++) {
                JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nFormalEducation.item(i));

                FormalEducation formalEducation = new FormalEducation();

                formalEducation.setStartDate(jo.getString("PBEGDAM"));
                formalEducation.setEndDate(jo.getString("PENDDAM"));
                formalEducation.setEducationalEstablishment(jo.getString("PSLARTM_TEXT"));
                formalEducation.setInstitueLocation(jo.getString("PINSTIM_TEXT"));
                formalEducation.setBranchOfStudy(jo.getString("PSLTP1M_TEXT"));
                formalEducation.setAction(jo.getString("PRESE1M"));

                formalEducations.add(formalEducation);
            }

            return formalEducations;
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }



}
