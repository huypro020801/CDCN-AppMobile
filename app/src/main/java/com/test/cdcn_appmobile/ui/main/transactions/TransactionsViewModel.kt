package com.test.cdcn_appmobile.ui.main.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cdcn_appmobile.data.models.Expenditure
import com.test.cdcn_appmobile.data.repository.ExpenditureRepository
import com.test.cdcn_appmobile.extension.toDayMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionsViewModel(private val expenditureRepository: ExpenditureRepository) :
    ViewModel() {

    companion object {
        private var listExpenditure: MutableLiveData<MutableList<Expenditure>?> =
            MutableLiveData<MutableList<Expenditure>?>()
        private var idDayChosen: MutableLiveData<Int> = MutableLiveData<Int>()
        private var idMonthChosen: MutableLiveData<Int> = MutableLiveData<Int>()
        private var idYearChosen: MutableLiveData<Int> = MutableLiveData<Int>()
    }

    fun getListExpenditure(): LiveData<MutableList<Expenditure>?> = listExpenditure

    fun getIdDayChosen(): LiveData<Int> = idDayChosen

    fun getIdMonthChosen(): LiveData<Int> = idMonthChosen

    fun getIdYearChosen(): LiveData<Int> = idYearChosen

    fun getListExpenditureFromServer(
        authToken: String,
        idUser: String,
        onSuccess: (String?) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            expenditureRepository.getExpenditure(
                authToken,
                idUser,
                "${idDayChosen.value?.toString()?.toDayMonth()}-${idMonthChosen.value?.toString()?.toDayMonth()}-${idYearChosen.value}"
            ).collect {
                listExpenditure.postValue(it.resultObj ?: listExpenditure.value)
                withContext(Dispatchers.Main) {
                    onSuccess(it.message)
                }
            }
        }
    }

    fun setIdDayChosen(idDay: Int) {
        idDayChosen.value = idDay
    }

    fun setIdMonthChosen(idMonth: Int) {
        idMonthChosen.value = idMonth
    }

    fun setIdYearChosen(idYear: Int) {
        idYearChosen.value = idYear
    }

}
