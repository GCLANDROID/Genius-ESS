package com.genius.employee.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kiit on 05-01-2018.
 */

public class Pref {
    private SharedPreferences _pref;
    private static final String PREF_FILE = "com.deus";
    private SharedPreferences.Editor _editorPref;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";


    @SuppressLint("CommitPrefEdits")
    public Pref(Context context) {
        _pref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        _editorPref = _pref.edit();
    }


    public void setFirstTimeLaunch(boolean isFirstTime) {
        _editorPref.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        _editorPref.commit();
    }

    public boolean isFirstTimeLaunch() {
        return _pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }



    public void saveFlag(String flag){
        _editorPref.putString("flag", flag);
        _editorPref.commit();
    }

    public String getFlag(){
        return _pref.getString("flag","");
    }

    public void saveEmpName(String name){
        _editorPref.putString("name", name);
        _editorPref.commit();
    }

    public String getEmpName(){
        return _pref.getString("name","");
    }

       public void saveCtime(String ctime){
        _editorPref.putString("ctime", ctime);
        _editorPref.commit();
    }

    public String getCtime(){
        return _pref.getString("ctime","");
    }

    public void saveloginTime(String ltime){
        _editorPref.putString("ltime", ltime);
        _editorPref.commit();
    }

    public String getloginTime(){
        return _pref.getString("ltime","");
    }


    public void saveEmpId(String empid){
        _editorPref.putString("empid", empid);
        _editorPref.commit();
    }

    public String getEmpId(){
        return _pref.getString("empid","");
    }

    public void saveSecureEmpId(String SecureEmpId){
        _editorPref.putString("SecureEmpId", SecureEmpId);
        _editorPref.commit();
    }

    public String getSecureEmpId(){
        return _pref.getString("SecureEmpId","");
    }

    public void saveMenu(String menu){
        _editorPref.putString("menu", menu);
        _editorPref.commit();
    }

    public String getMenu(){
        return _pref.getString("menu","");
    }

    public void saveEmpConId(String emconid){
        _editorPref.putString("emconid", emconid);
        _editorPref.commit();
    }

    public String getEmpConId(){
        return _pref.getString("emconid","");
    }


    public void saveEmpClintId(String emcclid){
        _editorPref.putString("emcclid", emcclid);
        _editorPref.commit();
    }

    public String getEmpClintId(){
        return _pref.getString("emcclid","");
    }

    public void saveEmpClintOffId(String emccloid){
        _editorPref.putString("emccloid", emccloid);
        _editorPref.commit();
    }

    public String getEmpClintOffId(){
        return _pref.getString("emccloid","");
    }

    public void saveMasterId(String Master){
        _editorPref.putString("Master", Master);
        _editorPref.commit();
    }

    public String getMasterId(){
        return _pref.getString("Master","");
    }

    public void saveUserType(String UserType){
        _editorPref.putString("UserType", UserType);
        _editorPref.commit();
    }

    public String getUserType(){
        return _pref.getString("UserType","");
    }


    public void saveCTCURL(String CTCURL){
        _editorPref.putString("CTCURL", CTCURL);
        _editorPref.commit();
    }

    public String getCTCURL(){
        return _pref.getString("CTCURL","");
    }


    public void saveManageId(String ManageId){
        _editorPref.putString("ManageId", ManageId);
        _editorPref.commit();
    }

    public String getManageId(){
        return _pref.getString("Weeklyoff","");
    }

    public void saveWeeklyoff(String Weeklyoff){
        _editorPref.putString("Weeklyoff", Weeklyoff);
        _editorPref.commit();
    }

    public String getWeeklyoff(){
        return _pref.getString("Weeklyoff","");
    }

    public void saveOnLeave(String OnLeave){
        _editorPref.putString("OnLeave", OnLeave);
        _editorPref.commit();
    }

    public String getOnLeave(){
        return _pref.getString("OnLeave","");
    }

    public void saveLeaveUrl(String LeaveUrl){
        _editorPref.putString("LeaveUrl", LeaveUrl);
        _editorPref.commit();
    }

    public String getLeaveUrl(){
        return _pref.getString("LeaveUrl","");
    }


    public void saveSecurityCode(String SecurityCode){
        _editorPref.putString("SecurityCode", SecurityCode);
        _editorPref.commit();
    }

    public String getSecurityCode(){
        return _pref.getString("SecurityCode","");
    }


    public void saveBackAttd(String BackAttd){
        _editorPref.putString("BackAttd", BackAttd);
        _editorPref.commit();
    }

    public String getBackAttd(){
        return _pref.getString("BackAttd","");
    }

    public void saveAttdImg(String AttdImg){
        _editorPref.putString("AttdImg", AttdImg);
        _editorPref.commit();
    }

    public String getAttdImg(){
        return _pref.getString("AttdImg","");
    }


    public void saveSup(String Sup){
        _editorPref.putString("Sup", Sup);
        _editorPref.commit();
    }

    public String getSup(){
        return _pref.getString("Sup","");
    }

    public void saveAtteType(String AtteType){
        _editorPref.putString("AtteType", AtteType);
        _editorPref.commit();
    }

    public String getAtteType(){
        return _pref.getString("AtteType","");
    }


    public void saveFlagLocation(String FlagLocation){
        _editorPref.putString("FlagLocation", FlagLocation);
        _editorPref.commit();
    }

    public String getFlagLocation(){
        return _pref.getString("FlagLocation","");
    }


    public void savePassword(String Password){
        _editorPref.putString("Password", Password);
        _editorPref.commit();
    }

    public String getPassword(){
        return _pref.getString("Password","");
    }


    public void saveCheckFlag(String ckflag){
        _editorPref.putString("ckflag", ckflag);
        _editorPref.commit();
    }

    public String getCheckFlag(){
        return _pref.getString("ckflag","");
    }


    public void saveSkill(String Skill){
        _editorPref.putString("Skill", Skill);
        _editorPref.commit();
    }

    public String getSkill(){
        return _pref.getString("Skill","");
    }



    public void saveRollTitle(String RollTitle){
        _editorPref.putString("RollTitle", RollTitle);
        _editorPref.commit();
    }

    public String getRollTitle(){
        return _pref.getString("RollTitle","");
    }

    public void saveFName(String FName){
        _editorPref.putString("FName", FName);
        _editorPref.commit();
    }

    public String getFName(){
        return _pref.getString("FName","");
    }

    public void saveLName(String LName){
        _editorPref.putString("LName", LName);
        _editorPref.commit();
    }

    public String getLName(){
        return _pref.getString("LName","");
    }

    public void saveDept(String Dept){
        _editorPref.putString("Dept", Dept);
        _editorPref.commit();
    }

    public String getDept(){
        return _pref.getString("Dept","");
    }

    public void saveBranch(String Branch){
        _editorPref.putString("Branch", Branch);
        _editorPref.commit();
    }

    public String getBranch(){
        return _pref.getString("Branch","");
    }

    public void saveDes(String Des){
        _editorPref.putString("Des", Des);
        _editorPref.commit();
    }

    public String getDes(){
        return _pref.getString("Des","");
    }

    public void saveFunction(String Function){
        _editorPref.putString("Function", Function);
        _editorPref.commit();
    }

    public String getFunction(){
        return _pref.getString("Function","");
    }

    public void saveDOJ(String DOJ){
        _editorPref.putString("DOJ", DOJ);
        _editorPref.commit();
    }

    public String getDOJ(){
        return _pref.getString("DOJ","");
    }

    public void saveEmpType(String EmpType){
        _editorPref.putString("EmpType", EmpType);
        _editorPref.commit();
    }

    public String getEmpType(){
        return _pref.getString("EmpType","");
    }


    public void saveSalutation(String Salutation){
        _editorPref.putString("Salutation", Salutation);
        _editorPref.commit();
    }

    public String getSalutation(){
        return _pref.getString("Salutation","");
    }



    public void savePhy(String Phy){
        _editorPref.putString("Phy", Phy);
        _editorPref.commit();
    }

    public String getPhy(){
        return _pref.getString("Phy","");
    }


    public void saveDOB(String DOB){
        _editorPref.putString("DOB", DOB);
        _editorPref.commit();
    }

    public String getDOB(){
        return _pref.getString("DOB","");
    }


    public void saveMartial(String Martial){
        _editorPref.putString("Martial", Martial);
        _editorPref.commit();
    }

    public String getMartial(){
        return _pref.getString("Martial","");
    }

    public void saveGender(String Gender){
        _editorPref.putString("Gender", Gender);
        _editorPref.commit();
    }

    public String getGender(){
        return _pref.getString("Gender","");
    }

    public void saveBlood(String Blood){
        _editorPref.putString("Blood", Blood);
        _editorPref.commit();
    }

    public String getBlood(){
        return _pref.getString("Blood","");
    }

    public void saveGurdian(String Gurdian){
        _editorPref.putString("Gurdian", Gurdian);
        _editorPref.commit();
    }

    public String getGurdian(){
        return _pref.getString("Gurdian","");
    }


    public void saveRelation(String Relation){
        _editorPref.putString("Relation", Relation);
        _editorPref.commit();
    }

    public String getRelation(){
        return _pref.getString("Relation","");
    }


    public void saveProfileImage(String ProfileImage){
        _editorPref.putString("ProfileImage", ProfileImage);
        _editorPref.commit();
    }

    public String getProfileImage(){
        return _pref.getString("ProfileImage","");
    }

    public void saveDeptId(String DeptId){
        _editorPref.putString("DeptId", DeptId);
        _editorPref.commit();
    }

    public String getDeptId(){
        return _pref.getString("DeptId","");
    }


    public void saveIntentFlag(String IntentFlag){
        _editorPref.putString("IntentFlag", IntentFlag);
        _editorPref.commit();
    }

    public String getIntentFlag(){
        return _pref.getString("IntentFlag","");
    }


    public void saveVersionHitFlag(String VersionHitFlag){
        _editorPref.putString("VersionHitFlag", VersionHitFlag);
        _editorPref.commit();
    }

    public String getVersionHitFlag(){
        return _pref.getString("VersionHitFlag","");
    }

    public void saveLoginFlag(String LoginFlag){
        _editorPref.putString("LoginFlag", LoginFlag);
        _editorPref.commit();
    }

    public String getLoginFlag(){
        return _pref.getString("LoginFlag","");
    }


    public void saveNormalFlag(String NormalFlag){
        _editorPref.putString("NormalFlag", NormalFlag);
        _editorPref.commit();
    }

    public String getNormalFlag(){
        return _pref.getString("NormalFlag","");
    }



    public void saveLogOutFlag(String LogOutFlag){
        _editorPref.putString("LogOutFlag", LogOutFlag);
        _editorPref.commit();
    }

    public String getLogOutFlag(){
        return _pref.getString("LogOutFlag","");
    }




    public void saveInFlag(String InFlag){
        _editorPref.putString("InFlag", InFlag);
        _editorPref.commit();
    }

    public String getInFlag(){
        return _pref.getString("InFlag","");
    }



    public void saveLebelId(String LebelId){
        _editorPref.putString("LebelId", LebelId);
        _editorPref.commit();
    }

    public String getLebelId(){
        return _pref.getString("LebelId","");
    }


    public void saveDepttId(String DepttId){
        _editorPref.putString("DeptId", DepttId);
        _editorPref.commit();
    }

    public String getDepttId(){
        return _pref.getString("DepttId","");
    }


    public void saveArraySize(int ArraySize){
        _editorPref.putInt("ArraySize", ArraySize);
        _editorPref.commit();
    }

    public int getArraySize(){
        return _pref.getInt("ArraySize",0);
    }

    public void saveBranchId(String BranchId){
        _editorPref.putString("BranchId", BranchId);
        _editorPref.commit();
    }

    public String getBranchId(){
        return _pref.getString("BranchId","");
    }



    public void saveDomainId(String DomainId){
        _editorPref.putString("DomainId", DomainId);
        _editorPref.commit();
    }

    public String getDomainId(){
        return _pref.getString("DomainId","");
    }


    public void saveFeedBackMasterId(String FeedBackMasterId){
        _editorPref.putString("FeedBackMasterId", FeedBackMasterId);
        _editorPref.commit();
    }

    public String getFeedBackMasterId(){
        return _pref.getString("FeedBackMasterId","");
    }

    public void saveFClintId(String FClintId){
        _editorPref.putString("FClintId", FClintId);
        _editorPref.commit();
    }

    public String getFClintId(){
        return _pref.getString("FClintId","");
    }



    public void saveClintOfficeName(String ClintOfficeName){
        _editorPref.putString("ClintOfficeName", ClintOfficeName);
        _editorPref.commit();
    }

    public String getClintOfficeName(){
        return _pref.getString("ClintOfficeName","");
    }


    public void saveOfficeName(String OfficeName){
        _editorPref.putString("OfficeName", OfficeName);
        _editorPref.commit();
    }

    public String getOfficeName(){
        return _pref.getString("OfficeName","");
    }


    public void saveFOfficeId(String fOfficeId){
        _editorPref.putString("fOfficeId", fOfficeId);
        _editorPref.commit();
    }

    public String getFOfficeId(){
        return _pref.getString("fOfficeId","");
    }


    public void saveOfficeId(String OfficeId) {
        _editorPref.putString("OfficeId", OfficeId);
        _editorPref.commit();
    }

    public String getOfficeId() {
        return _pref.getString("OfficeId", "");
    }


    public void saveFClintName(String FClintName){
        _editorPref.putString("FClintName", FClintName);
        _editorPref.commit();
    }

    public String getFClintName(){
        return _pref.getString("FClintName","");
    }


    public void saveClintPhn(String ClintPhn){
        _editorPref.putString("ClintPhn", ClintPhn);
        _editorPref.commit();
    }

    public String getClintPhn(){
        return _pref.getString("ClintPhn","");
    }


    public void saveClintDes(String ClintDes){
        _editorPref.putString("ClintDes", ClintDes);
        _editorPref.commit();
    }

    public String getClintDes(){
        return _pref.getString("ClintDes","");
    }

    public void saveClintEmail(String ClintEmail){
        _editorPref.putString("ClintEmail", ClintEmail);
        _editorPref.commit();
    }
    public String getClintEmail(){
        return _pref.getString("ClintEmail","");
    }

    public void saveAns(String Ans){
        _editorPref.putString("Ans", Ans);
        _editorPref.commit();
    }

    public String getAns(){
        return _pref.getString("Ans","");
    }

    public void saveAns1(String Ans1){
        _editorPref.putString("Ans1", Ans1);
        _editorPref.commit();
    }

    public String getAns1(){
        return _pref.getString("Ans1","");
    }

    public void saveRemark(String Remark) {
        _editorPref.putString("Remark", Remark);
        _editorPref.commit();
    }

    public String getRemark() {
        return _pref.getString("Remark", "");

    }

    public void saveFeedbackEmpId(String FeedbackEmpId){
        _editorPref.putString("FeedbackEmpId", FeedbackEmpId);
        _editorPref.commit();
    }

    public String getFeedbackEmpId(){
        return _pref.getString("FeedbackEmpId","");
    }

    public void saveAccNo(String AccNo){
        _editorPref.putString("AccNo", AccNo);
        _editorPref.commit();
    }

    public String getAccNo(){
        return _pref.getString("AccNo","");
    }


    public void savePFNO(String PFNO){
        _editorPref.putString("PFNO", PFNO);
        _editorPref.commit();
    }

    public String getPFNO(){
        return _pref.getString("PFNO","");
    }


    public void saveESICNO(String ESICNO){
        _editorPref.putString("ESICNO", ESICNO);
        _editorPref.commit();
    }

    public String getESICNO(){
        return _pref.getString("ESICNO","");
    }


    public void savePANO(String PANO){
        _editorPref.putString("PANO", PANO);
        _editorPref.commit();
    }

    public String getPANO(){
        return _pref.getString("PANO","");
    }

    public void saveAadhar(String Aadhar){
        _editorPref.putString("Aadhar", Aadhar);
        _editorPref.commit();
    }

    public String getAadhar(){
        return _pref.getString("Aadhar","");
    }


    public void saveDialogClick(String DialogClick){
        _editorPref.putString("DialogClick", DialogClick);
        _editorPref.commit();
    }

    public String getDialogClick(){
        return _pref.getString("DialogClick","");
    }

    public void saveAccessToken(String access_token){
        _editorPref.putString("access_token", access_token);
        _editorPref.commit();
    }

    public String getAccessToken(){
        return _pref.getString("access_token","");
    }





}

