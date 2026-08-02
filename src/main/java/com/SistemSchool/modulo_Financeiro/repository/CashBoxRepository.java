package com.SistemSchool.modulo_Financeiro.repository;

import com.SistemSchool.modulo_Financeiro.dto.CashBoxDTO;
import com.SistemSchool.modulo_Financeiro.interfaces.CashBoxTableProjection;
import com.SistemSchool.modulo_Financeiro.io.CashBoxStatus;
import com.SistemSchool.modulo_Financeiro.model.CashBox;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CashBoxRepository extends JpaRepository<CashBox, Long> {

    // =====================================================
    // Lazy Loading para PrimeFaces DataTable
    // =====================================================

    @Query(value = """

            SELECT


                cb.ph_cash_box AS phCashBox,


                cb.cash_box_number AS cashBoxNumber,



                cb.operator AS operator,



                cb.opening_balance AS openingBalance,



                COALESCE(SUM(
                    CASE
                        WHEN fm.type = 'INCOME'
                        THEN fm.amount
                        ELSE 0
                    END
                ),0) AS totalIncome,



                COALESCE(SUM(
                    CASE
                        WHEN fm.type = 'EXPENSE'
                        THEN fm.amount
                        ELSE 0
                    END
                ),0) AS totalExpense,



                (
                    cb.opening_balance +

                    COALESCE(SUM(
                        CASE
                            WHEN fm.type = 'INCOME'
                            THEN fm.amount
                            ELSE 0
                        END
                    ),0)

                    -

                    COALESCE(SUM(
                        CASE
                            WHEN fm.type = 'EXPENSE'
                            THEN fm.amount
                            ELSE 0
                        END
                    ),0)

                ) AS currentBalance,



                cb.status AS status,



                cb.opening_date AS openingDate,



                cb.closing_date AS closingDate,



                cb.created_at AS createdAt,


                cb.updated_at AS updatedAt



            FROM cash_box cb



            LEFT JOIN financial_movement fm

            ON fm.cash_box_pk = cb.ph_cash_box



            GROUP BY

                cb.ph_cash_box



            """,

            countQuery = """

                    SELECT COUNT(*)

                    FROM cash_box

                    """,

            nativeQuery = true)

    Page<CashBoxTableProjection> findAllForTable(Pageable pageable);

    // =====================================================
    // Lista completa usando DTO
    //
    // Usa o construtor reduzido de CashBoxDTO (sem totalIncome/totalExpense/
    // currentBalance calculados, sem consultar financial_movement) — essa
    // listagem serve apenas para estatísticas do dashboard e para localizar
    // um caixa por id (editar/excluir/fechar). Os valores calculados a
    // partir dos movimentos financeiros vêm de findAllForTable (lazy).
    // =====================================================

    @Query("""

            SELECT new com.SistemSchool.modulo_Financeiro.dto.CashBoxDTO(


                cb.phCashBox,


                cb.cashBoxNumber,



                cb.openingBalance,



                cb.closingBalance,



                cb.operator,



                cb.status,



                cb.openingDate,



                cb.closingDate,



                cb.observation,



                cb.createdAt,



                cb.updatedAt


            )


            FROM CashBox cb


            """)

    List<CashBoxDTO> findAllCashBoxesDTO();

    @Query("SELECT COALESCE(SUM(c.openingBalance + c.totalIncome - c.totalExpense), 0) " +
            "FROM CashBox c WHERE c.status = com.SistemSchool.modulo_Financeiro.io.CashBoxStatus.OPEN")
    BigDecimal sumSaldoCaixasAbertos();
    // =====================================================
    // Consultas utilitárias
    // =====================================================

    List<CashBox> findByStatus(CashBoxStatus status);

    List<CashBox> findByOperator(String operator);

    List<CashBox> findByOpeningDate(LocalDate openingDate);

    List<CashBox> findByOpeningDateBetween(
            LocalDate startDate,
            LocalDate endDate);

    boolean existsByCashBoxNumber(String cashBoxNumber);

    /**
     * Verifica se existe caixa aberto
     */
    boolean existsByStatus(CashBoxStatus status);

    /**
     * Buscar último caixa aberto
     */
    CashBox findFirstByStatusOrderByOpeningDateDesc(
            CashBoxStatus status);

    /**
     * Somatório de saldo inicial dos caixas
     */
    @Query("""

            SELECT SUM(cb.openingBalance)

            FROM CashBox cb

            WHERE cb.status =
            com.SistemSchool.modulo_Financeiro.io.CashBoxStatus.OPEN

            """)

    BigDecimal getTotalOpeningBalance();

}