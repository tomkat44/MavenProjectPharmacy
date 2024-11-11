package com.example.app.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "drugs")
public class Drug {

        @Id
        @Column(name="id")
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Integer id;

        @Column(name="drugName", length=200, nullable=false)
        private String drugName;

        @Column(name="drugPrice", length=100, nullable=false)
        private double drugPrice;

        @Enumerated(EnumType.STRING)
        @Column(name = "MedicineCategory")
        private medicineCategory medicineCategory;

        /*Εδώ θα προσθέσω το Foreign Key
        * Ότι δηλαδή κάθε φάρματο θα έχει μέσα στον πίνακά του το ID του ActiveSubstance*/



        /*Του λέω επιπλέον πως θα λέγετια το id με το οποίο θα συνδεθεί μέσα στο
        * πίνακα του DRUG
        * και Του λέω ότι θα είναι nullable αφού κάθε φράμακο έχει δραστική ουσία*/
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "activeSubstance_id", nullable = false)
        private ActiveSubstance activeSubstance;

        @OneToMany(mappedBy = "drug", fetch = FetchType.LAZY)
        private Set<QuantityPrescription> quantityPrescriptions = new HashSet<QuantityPrescription>();

        @OneToMany(mappedBy = "drug", fetch = FetchType.LAZY)
        private Set<QuantityExecution> quantityExecutions = new HashSet<QuantityExecution>();


        public Drug(String drugName, Double drugPrice, medicineCategory medicineCategory){
            this.drugName = drugName;
            this.drugPrice = drugPrice;
            this.medicineCategory = medicineCategory;
        }
        public Drug(){
        }

        public Integer getId() {
                return id;
        }


        public void setId(Integer id){this.id=id;}


        public String getDrugName() {
                return drugName;
        }

        public void setDrugName(String drugName) {
                this.drugName = drugName;
        }

        public double getDrugPrice() {
                return drugPrice;
        }

        public void setDrugPrice(double drugPrice) {
                this.drugPrice = drugPrice;
        }

        public com.example.app.domain.medicineCategory getMedicineCategory() {
                return medicineCategory;
        }

        public void setMedicineCategory(com.example.app.domain.medicineCategory medicineCategory) {
                this.medicineCategory = medicineCategory;
        }

        public ActiveSubstance getActiveSubstance() {
                return activeSubstance;
        }

        public void setActiveSubstance(ActiveSubstance activeSubstance) {
                this.activeSubstance = activeSubstance;
        }

        /*Εδώ θέλω να διαργάφονται αυτόματα τα QuantityPrescriptions
        * κάθε φορά που διαγράφο ένα φάρμακο μέσα από το prescription
        * Έτσι όταν μία συνταγή καλέσει τα φάρμακα τότε το φάρμακο
        * που είναι ενωμένο με ManyToOne με  το QuantityPrescriptions
        * θα επιστρέψει εκέινο το QuantityPrescription που εντιστοιχεί
        * στο ID του DRUG.
        * Δεν υπάρχει δεύτερο QuantityPrescription με το ίδιο ID μέσα
        * στην ίδια συνταγή αλλιώς πρόκειται για διπλοεγγραφή και
        * καλά κάνει και το διαγράφει*/

        public Set<QuantityPrescription> getQuantityPrescriptions() {
                return quantityPrescriptions;
        }

        public void addQuantityPrescription(QuantityPrescription quantityPrescription) {
                if(quantityPrescription != null) {
                        quantityPrescriptions.add(quantityPrescription);
                }
        }
        /*Καλείται από την RemoveDrug της Prescription για να πάρει τα QuantityPrescription για να τα διαγράψει
        Δέχεται σαν ορίσματα το id του Prescription και τις QuantityPrescriptions που έχει μέσα το Prescription\
        * και αν το QP δεν ειναι null και έχει μέσα σοτυ τιμές*/
//        public QuantityPrescription getQuantityPrescriptionFromDrug(int prescription_id, Set<QuantityPrescription> quantityPrescription) {
//                QuantityPrescription qps = null;
//                if (quantityPrescription != null) {
//                        System.out.println("DRUGid = "+ id + "\tQPid = "+ quantityPrescription.size() );
//                        for (QuantityPrescription qp : quantityPrescriptions) {
//
//                                //System.out.println("DRUGid = "+ id + "\tQPid = "+ qp.getDrug().getId() );
//                                //System.out.println("Prescription id = "+ prescription_id + "\tPid = "+ qp.getPrescription().getId() );
//
//                                /*Ελέγχει ότι το id του Drug σε αυτή την κλάση με το DrugID που του έστειλα
//                                 * Ελέγχει ότι το QP_id είεναι το ίδιο με αυτό που υπαρχει και στο Prescription
//                                 * και επιστρέφει το QuantityPrescription που είναι να διαγραφεί*/
//                                if (id == qp.getDrug().getId() && prescription_id==qp.getPrescription().getId()) {
//                                        qps = qp;
//                                }
//                        }
//                }
//                return qps;
//        }

        /*Υλοποιεί μία γραμμική αναζήτηση ότι αν κάποιο φάρμακο έχει το
        * ίδιο όνομα με άλλο τότε θα καλέιται και θα επιστρέφει FALSE
        * και δεν θα αφήνει την καταχώρησή του αφού υπάρχει ήδη
        * κανονικά είναι σε επίπεδο DB αυτό.*/
        public boolean drugDoesNotExist(Drug drug, List<Drug> listOfAllDrugs){
                boolean flag = true;
                for(Drug d : listOfAllDrugs){
                        if(drug.equals(d)){
                                flag = false;
                        }
                        if(!flag){
                                break;
                        }
                }

                return flag;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Drug)) return false;
                Drug drug = (Drug) o;
                return Objects.equals(drugName, drug.drugName); //&& Objects.equals(drugPrice, drug.drugPrice) ;
        }

        @Override
        public int hashCode() {
                return Objects.hash(drugName, drugPrice);
        }
}
