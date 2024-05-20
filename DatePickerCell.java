package ClasseBase;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class DatePickerCell extends DateCell {

    private final LocalDate minDate;

    // Constructeur pour DatePickerCell sans restriction de date minimale
    public DatePickerCell() {
        this.minDate = null;
    }

    // Constructeur pour DatePickerCell avec une restriction de date minimale
    public DatePickerCell(LocalDate minDate) {
        this.minDate = minDate;
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);

        // Désactiver les dates passées
        if (item.isBefore(LocalDate.now())) {
            setDisable(true);
            setStyle("-fx-background-color: #ffc0cb;"); // Couleur de fond désactivée pour les dates passées
        }

        // Désactiver les dates avant la date minimale
        if (minDate != null && item.isBefore(minDate)) {
            setDisable(true);
            setStyle("-fx-background-color: #ffc0cb;"); // Couleur de fond désactivée pour les dates avant la date minimale
        }
    }
}
