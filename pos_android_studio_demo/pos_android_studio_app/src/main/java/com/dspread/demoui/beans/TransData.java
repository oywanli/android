package com.dspread.demoui.beans;

import java.io.Serializable;

public class TransData implements Serializable {
   private String posInfo;
   private String posId;
   private String updateCheckValue;
   private String keyCheckValue;
   private String inputMoney;
   private String payType;
   private String payment;
   private String SN;
   private String cashbackAmounts;

   private int sub;
   private int successSub;
   private int fialSub;
   private String autoTrade;
   public String getCashbackAmounts() {
      return cashbackAmounts;
   }

   public void setCashbackAmounts(String cashbackAmounts) {
      this.cashbackAmounts = cashbackAmounts;
   }

   public String getSN() {
      return SN;
   }

   public void setSN(String SN) {
      this.SN = SN;
   }

   public String getInputMoney() {
      return inputMoney;
   }

   public void setInputMoney(String inputMoney) {
      this.inputMoney = inputMoney;
   }

   public String getPayType() {
      return payType;
   }

   public void setPayType(String payType) {
      this.payType = payType;
   }

   public String getPayment() {
      return payment;
   }

   public void setPayment(String payment) {
      this.payment = payment;
   }

   public String getPosInfo() {
      return posInfo;
   }

   public void setPosInfo(String posInfo) {
      this.posInfo = posInfo;
   }

   public String getUpdateCheckValue() {
      return updateCheckValue;
   }

   public void setUpdateCheckValue(String updateCheckValue) {
      this.updateCheckValue = updateCheckValue;
   }

   public String getKeyCheckValue() {
      return keyCheckValue;
   }

   public void setKeyCheckValue(String keyCheckValue) {
      this.keyCheckValue = keyCheckValue;
   }

   public String getPosId() {
      return posId;
   }

   public void setPosId(String posId) {
      this.posId = posId;
   }

   public int getSub() {
      return sub;
   }

   public void setSub(int sub) {
      this.sub = sub;
   }

   public int getSuccessSub() {
      return successSub;
   }

   public void setSuccessSub(int successSub) {
      this.successSub = successSub;
   }

   public int getFialSub() {
      return fialSub;
   }

   public void setFialSub(int fialSub) {
      this.fialSub = fialSub;
   }

   public String getAutoTrade() {
      return autoTrade;
   }

   public void setAutoTrade(String autoTrade) {
      this.autoTrade = autoTrade;
   }
}