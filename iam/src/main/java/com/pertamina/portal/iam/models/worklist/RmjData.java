package com.pertamina.portal.iam.models.worklist;

import com.pertamina.portal.iam.activity.RmjDetailActivity;

import java.io.Serializable;

public class RmjData implements Serializable {
    public String panNumber, namaPekerja, kodeJabatan, namaJabatan, jabatanLama, jenisMutasi;
    public int prlBsBaru, prlJabatanBaru;

    public String PersonID, NOPEK, PCNAMEM, PGBDATM, TMT_JABATAN, TMT_PRL_BS_OLD, AGE, HIRING_DATE, TMT_PENSIUN, FIRSTSCORE, SECONDSCORE;

    public String PPLANSM, PPLTXTM, PRL, PPERSAM, PPERTXM, PBTRTLM, PBTEXTM, PDEPTXM, PDIVTXM, PFUNTXM, PDIRTXM, PBUKRSM, PBUTXTM, TMT_PRL_BS;

    public String TMT_ADMIN_BEGDA, TYPE_MUTASI, KETERANGAN_MUTASI, ID_POSISI_TUJUAN, PPLTXTM_NEW, PRL_NEW, PPERSAM_NEW, PBTRTLM_NEW, PDEPTXM_NEW;
    public String PDIVTXM_NEW, PFUNTXM_NEW, PDIRTXM_NEW, PBUKRSM_NEW, PBUTXTM_NEW;
}
