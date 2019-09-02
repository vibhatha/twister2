licenses(["notice"])

package(default_visibility = ["//visibility:public"])

bin_files = ["bin/mpif90",
             "bin/aggregate_profile.pl",
             "bin/profile2mat.pl",
             "bin/ortecc",
             "bin/ompi-server",
             "bin/ompi_info",
             "bin/orte-info",
             "bin/mpic++",
             "bin/orte-clean",
             "bin/mpif77",
             "bin/orterun",
             "bin/mpicxx",
             "bin/mpiexec",
             "bin/mpicc",
             "bin/ompi-clean",
             "bin/mpijavac.pl",
             "bin/orte-server",
             "bin/mpirun",
             "bin/mpifort",
             "bin/opal_wrapper",
             "bin/mpijavac",
             "bin/mpiCC",
             "bin/orted",]

etc_files = ["etc/openmpi-default-hostfile",
             "etc/pmix-mca-params.conf",
             "etc/openmpi-mca-params.conf",
             "etc/openmpi-totalview.tcl",]

include_files = ["include/mpif.h",
                 "include/openmpi/ompi/mpi/java/mpiJava.h",
                 "include/openmpi/mpiext/mpiext_pcollreq_c.h",
                 "include/openmpi/mpiext/mpiext_affinity_c.h",
                 "include/openmpi/mpiext/pmpiext_pcollreq_c.h",
                 "include/openmpi/mpiext/mpiext_cuda_c.h",
                 "include/mpi_portable_platform.h",
                 "include/mpi.h",
                 "include/mpif-ext.h",
                 "include/mpif-sizeof.h",
                 "include/mpi-ext.h",
                 "include/mpif-c-constants-decl.h",]

lib_files = ["lib/libmca_common_sm.la",
             "lib/openmpi/mca_fcoll_vulcan.so",
             "lib/openmpi/mca_mpool_hugepage.la",
             "lib/openmpi/mca_sharedfp_lockedfile.so",
             "lib/openmpi/mca_schizo_slurm.so",
             "lib/openmpi/mca_state_app.so",
             "lib/openmpi/mca_odls_pspawn.so",
             "lib/openmpi/mca_oob_tcp.so",
             "lib/openmpi/mca_regx_fwd.so",
             "lib/openmpi/mca_allocator_bucket.so",
             "lib/openmpi/mca_fcoll_dynamic_gen2.la",
             "lib/openmpi/mca_ess_slurm.la",
             "lib/openmpi/mca_coll_libnbc.so",
             "lib/openmpi/mca_coll_monitoring.so",
             "lib/openmpi/mca_grpcomm_direct.so",
             "lib/openmpi/mca_ess_tool.la",
             "lib/openmpi/mca_coll_tuned.so",
             "lib/openmpi/mca_odls_default.la",
             "lib/openmpi/mca_routed_direct.la",
             "lib/openmpi/mca_btl_sm.la",
             "lib/openmpi/mca_plm_slurm.la",
             "lib/openmpi/mca_mpool_hugepage.so",
             "lib/openmpi/mca_rmaps_resilient.la",
             "lib/openmpi/mca_ras_simulator.la",
             "lib/openmpi/mca_schizo_ompi.so",
             "lib/openmpi/mca_errmgr_default_hnp.la",
             "lib/openmpi/mca_fcoll_two_phase.la",
             "lib/openmpi/mca_dfs_orted.la",
             "lib/openmpi/mca_rmaps_mindist.la",
             "lib/openmpi/mca_compress_gzip.la",
             "lib/openmpi/mca_oob_tcp.la",
             "lib/openmpi/mca_vprotocol_pessimist.la",
             "lib/openmpi/mca_odls_default.so",
             "lib/openmpi/mca_iof_tool.la",
             "lib/openmpi/mca_crs_none.so",
             "lib/openmpi/mca_rmaps_round_robin.so",
             "lib/openmpi/mca_errmgr_default_orted.la",
             "lib/openmpi/mca_topo_basic.so",
             "lib/openmpi/mca_plm_isolated.la",
             "lib/openmpi/mca_fcoll_individual.so",
             "lib/openmpi/mca_rmaps_ppr.so",
             "lib/openmpi/mca_regx_fwd.la",
             "lib/openmpi/mca_grpcomm_direct.la",
             "lib/openmpi/mca_dfs_test.la",
             "lib/openmpi/mca_rcache_grdma.la",
             "lib/openmpi/mca_sharedfp_sm.so",
             "lib/openmpi/mca_ess_slurm.so",
             "lib/openmpi/mca_coll_self.so",
             "lib/openmpi/mca_rmaps_rank_file.la",
             "lib/openmpi/mca_schizo_orte.la",
             "lib/openmpi/mca_state_novm.la",
             "lib/openmpi/mca_reachable_weighted.la",
             "lib/openmpi/mca_allocator_basic.so",
             "lib/openmpi/mca_ess_pmi.la",
             "lib/openmpi/mca_fcoll_dynamic.so",
             "lib/openmpi/mca_ess_env.la",
             "lib/openmpi/mca_ess_env.so",
             "lib/openmpi/mca_coll_sm.so",
             "lib/openmpi/mca_coll_sync.la",
             "lib/openmpi/mca_pml_monitoring.la",
             "lib/openmpi/mca_schizo_orte.so",
             "lib/openmpi/mca_osc_rdma.so",
             "lib/openmpi/mca_notifier_syslog.la",
             "lib/openmpi/mca_compress_gzip.so",
             "lib/openmpi/mca_errmgr_default_app.so",
             "lib/openmpi/mca_sharedfp_individual.la",
             "lib/openmpi/mca_state_hnp.so",
             "lib/openmpi/mca_coll_sync.so",
             "lib/openmpi/mca_pml_cm.so",
             "lib/openmpi/mca_schizo_flux.la",
             "lib/openmpi/mca_errmgr_default_tool.so",
             "lib/openmpi/mca_coll_monitoring.la",
             "lib/openmpi/mca_pmix_pmix3x.so",
             "lib/openmpi/mca_pmix_isolated.so",
             "lib/openmpi/mca_pml_ob1.so",
             "lib/openmpi/mca_allocator_bucket.la",
             "lib/openmpi/mca_rmaps_rank_file.so",
             "lib/openmpi/mca_state_tool.la",
             "lib/openmpi/mca_regx_reverse.la",
             "lib/openmpi/mca_rmaps_ppr.la",
             "lib/openmpi/mca_rmaps_seq.so",
             "lib/openmpi/mca_shmem_posix.so",
             "lib/openmpi/mca_sharedfp_individual.so",
             "lib/openmpi/mca_sharedfp_sm.la",
             "lib/openmpi/mca_coll_inter.so",
             "lib/openmpi/mca_compress_bzip.la",
             "lib/openmpi/mca_crs_none.la",
             "lib/openmpi/mca_compress_bzip.so",
             "lib/openmpi/mca_dfs_app.la",
             "lib/openmpi/mca_fcoll_dynamic.la",
             "lib/openmpi/mca_pmix_isolated.la",
             "lib/openmpi/mca_ras_simulator.so",
             "lib/openmpi/mca_shmem_posix.la",
             "lib/openmpi/mca_plm_rsh.so",
             "lib/openmpi/mca_shmem_sysv.so",
             "lib/openmpi/mca_osc_pt2pt.la",
             "lib/openmpi/mca_fbtl_posix.so",
             "lib/openmpi/mca_errmgr_default_tool.la",
             "lib/openmpi/mca_coll_libnbc.la",
             "lib/openmpi/mca_osc_sm.so",
             "lib/openmpi/mca_ess_hnp.so",
             "lib/openmpi/mca_routed_debruijn.la",
             "lib/openmpi/mca_state_orted.so",
             "lib/openmpi/mca_state_tool.so",
             "lib/openmpi/mca_errmgr_default_orted.so",
             "lib/openmpi/mca_osc_rdma.la",
             "lib/openmpi/mca_io_ompio.so",
             "lib/openmpi/mca_bml_r2.la",
             "lib/openmpi/mca_dfs_test.so",
             "lib/openmpi/mca_btl_vader.so",
             "lib/openmpi/mca_io_romio321.la",
             "lib/openmpi/mca_allocator_basic.la",
             "lib/openmpi/libompi_dbg_msgq.so",
             "lib/openmpi/mca_vprotocol_pessimist.so",
             "lib/openmpi/mca_topo_treematch.la",
             "lib/openmpi/mca_plm_rsh.la",
             "lib/openmpi/mca_errmgr_default_hnp.so",
             "lib/openmpi/mca_coll_sm.la",
             "lib/openmpi/mca_btl_tcp.la",
             "lib/openmpi/mca_state_app.la",
             "lib/openmpi/mca_shmem_mmap.la",
             "lib/openmpi/mca_fs_ufs.so",
             "lib/openmpi/mca_ess_singleton.so",
             "lib/openmpi/mca_iof_orted.so",
             "lib/openmpi/mca_btl_tcp.so",
             "lib/openmpi/mca_fcoll_vulcan.la",
             "lib/openmpi/mca_io_romio321.so",
             "lib/openmpi/mca_rmaps_round_robin.la",
             "lib/openmpi/mca_shmem_mmap.so",
             "lib/openmpi/mca_pmix_flux.so",
             "lib/openmpi/mca_btl_vader.la",
             "lib/openmpi/mca_pmix_pmix3x.la",
             "lib/openmpi/mca_rtc_hwloc.la",
             "lib/openmpi/mca_sharedfp_lockedfile.la",
             "lib/openmpi/mca_dfs_app.so",
             "lib/openmpi/mca_fcoll_two_phase.so",
             "lib/openmpi/mca_rmaps_seq.la",
             "lib/openmpi/mca_schizo_ompi.la",
             "lib/openmpi/mca_fbtl_posix.la",
             "lib/openmpi/mca_state_novm.so",
             "lib/openmpi/mca_filem_raw.la",
             "lib/openmpi/mca_plm_isolated.so",
             "lib/openmpi/mca_osc_sm.la",
             "lib/openmpi/mca_iof_orted.la",
             "lib/openmpi/mca_iof_hnp.la",
             "lib/openmpi/mca_routed_debruijn.so",
             "lib/openmpi/mca_patcher_overwrite.so",
             "lib/openmpi/mca_bml_r2.so",
             "lib/openmpi/mca_btl_sm.so",
             "lib/openmpi/mca_routed_radix.la",
             "lib/openmpi/mca_filem_raw.so",
             "lib/openmpi/mca_rml_oob.la",
             "lib/openmpi/mca_iof_hnp.so",
             "lib/openmpi/mca_routed_radix.so",
             "lib/openmpi/mca_osc_monitoring.la",
             "lib/openmpi/mca_reachable_weighted.so",
             "lib/openmpi/mca_rcache_grdma.so",
             "lib/openmpi/mca_coll_self.la",
             "lib/openmpi/mca_fcoll_dynamic_gen2.so",
             "lib/openmpi/mca_pstat_linux.la",
             "lib/openmpi/mca_rml_oob.so",
             "lib/openmpi/mca_pml_cm.la",
             "lib/openmpi/libompi_dbg_msgq.la",
             "lib/openmpi/mca_pml_monitoring.so",
             "lib/openmpi/mca_coll_basic.la",
             "lib/openmpi/mca_btl_self.la",
             "lib/openmpi/mca_rmaps_mindist.so",
             "lib/openmpi/mca_io_ompio.la",
             "lib/openmpi/mca_errmgr_default_app.la",
             "lib/openmpi/mca_rmaps_resilient.so",
             "lib/openmpi/mca_ess_hnp.la",
             "lib/openmpi/mca_osc_monitoring.so",
             "lib/openmpi/mca_state_orted.la",
             "lib/openmpi/mca_fcoll_individual.la",
             "lib/openmpi/mca_iof_tool.so",
             "lib/openmpi/mca_ras_slurm.so",
             "lib/openmpi/mca_schizo_slurm.la",
             "lib/openmpi/mca_dfs_orted.so",
             "lib/openmpi/mca_topo_basic.la",
             "lib/openmpi/mca_shmem_sysv.la",
             "lib/openmpi/mca_routed_binomial.so",
             "lib/openmpi/mca_ess_pmi.so",
             "lib/openmpi/mca_pml_ob1.la",
             "lib/openmpi/mca_fs_ufs.la",
             "lib/openmpi/mca_osc_pt2pt.so",
             "lib/openmpi/mca_odls_pspawn.la",
             "lib/openmpi/mca_rtc_hwloc.so",
             "lib/openmpi/mca_state_hnp.la",
             "lib/openmpi/mca_patcher_overwrite.la",
             "lib/openmpi/mca_ess_tool.so",
             "lib/openmpi/mca_ras_slurm.la",
             "lib/openmpi/mca_regx_reverse.so",
             "lib/openmpi/mca_plm_slurm.so",
             "lib/openmpi/mca_notifier_syslog.so",
             "lib/openmpi/mca_btl_self.so",
             "lib/openmpi/mca_coll_tuned.la",
             "lib/openmpi/mca_coll_inter.la",
             "lib/openmpi/mca_routed_direct.so",
             "lib/openmpi/mca_pmix_flux.la",
             "lib/openmpi/mca_schizo_flux.so",
             "lib/openmpi/mca_coll_basic.so",
             "lib/openmpi/mca_ess_singleton.la",
             "lib/openmpi/mca_topo_treematch.so",
             "lib/openmpi/mca_routed_binomial.la",
             "lib/openmpi/mca_pstat_linux.so",
             "lib/libompitrace.so",
             "lib/libmpi_java.la",
             "lib/libmca_common_monitoring.so",
             "lib/libopen-pal.so.40",
             "lib/libmca_common_monitoring.so.50",
             "lib/libmca_common_sm.so.40",
             "lib/libmca_common_sm.so.40.20.0",
             "lib/libmca_common_dstore.la",
             "lib/libmca_common_monitoring.so.50.10.0",
             "lib/libompitrace.so.40.20.0",
             "lib/libompitrace.la",
             "lib/libmca_common_monitoring.la",
             "lib/libompitrace.so.40",
             "lib/libmpi_java.so",
             "lib/libmpi.la",
             "lib/libmca_common_sm.so",
             "lib/libmpi.so",
             "lib/libopen-rte.so",
             "lib/pkgconfig/ompi-cxx.pc",
             "lib/pkgconfig/orte.pc",
             "lib/pkgconfig/ompi-f90.pc",
             "lib/pkgconfig/ompi.pc",
             "lib/pkgconfig/ompi-fort.pc",
             "lib/pkgconfig/ompi-c.pc",
             "lib/pkgconfig/ompi-f77.pc",
             "lib/libmpi_java.so.40.20.0",
             "lib/libmpi.so.40",
             "lib/libmca_common_ompio.so.41.19.1",
             "lib/ompi_monitoring_prof.so",
             "lib/ompi_monitoring_prof.la",
             "lib/libmca_common_dstore.so.1.0.0",
             "lib/libmca_common_dstore.so.1",
             "lib/libmpi_java.so.40",
             "lib/libopen-rte.so.40.20.1",
             "lib/libmca_common_ompio.la",
             "lib/libopen-rte.so.40",
             "lib/libopen-rte.la",
             "lib/libmca_common_ompio.so.41",
             "lib/libmpi.so.40.20.1",
             "lib/libopen-pal.so",
             "lib/libopen-pal.la",
             "lib/libmca_common_dstore.so",
             "lib/libmca_common_ompio.so",
             "lib/pmix/mca_psec_native.so",
             "lib/pmix/mca_pnet_tcp.so",
             "lib/pmix/mca_gds_ds12.so",
             "lib/pmix/mca_psensor_heartbeat.so",
             "lib/pmix/mca_pnet_test.so",
             "lib/pmix/mca_ptl_usock.so",
             "lib/pmix/mca_preg_native.la",
             "lib/pmix/mca_gds_hash.so",
             "lib/pmix/mca_preg_native.so",
             "lib/pmix/mca_psec_none.la",
             "lib/pmix/mca_plog_stdfd.so",
             "lib/pmix/mca_gds_hash.la",
             "lib/pmix/mca_plog_default.la",
             "lib/pmix/mca_bfrops_v21.la",
             "lib/pmix/mca_psensor_file.so",
             "lib/pmix/mca_bfrops_v21.so",
             "lib/pmix/mca_bfrops_v20.so",
             "lib/pmix/mca_psec_none.so",
             "lib/pmix/mca_plog_default.so",
             "lib/pmix/mca_pshmem_mmap.so",
             "lib/pmix/mca_ptl_usock.la",
             "lib/pmix/mca_bfrops_v3.la",
             "lib/pmix/mca_pnet_tcp.la",
             "lib/pmix/mca_bfrops_v12.so",
             "lib/pmix/mca_psec_native.la",
             "lib/pmix/mca_ptl_tcp.la",
             "lib/pmix/mca_gds_ds12.la",
             "lib/pmix/mca_plog_syslog.so",
             "lib/pmix/mca_bfrops_v3.so",
             "lib/pmix/mca_psensor_file.la",
             "lib/pmix/mca_gds_ds21.so",
             "lib/pmix/mca_psensor_heartbeat.la",
             "lib/pmix/mca_pnet_test.la",
             "lib/pmix/mca_ptl_tcp.so",
             "lib/pmix/mca_bfrops_v12.la",
             "lib/pmix/mca_plog_stdfd.la",
             "lib/pmix/mca_plog_syslog.la",
             "lib/pmix/mca_bfrops_v20.la",
             "lib/pmix/mca_pshmem_mmap.la",
             "lib/pmix/mca_gds_ds21.la",
             "lib/libopen-pal.so.40.20.1",]

jar_files = ["lib/mpi.jar",]

out_files = bin_files + etc_files + include_files + lib_files + jar_files

genrule(
    name = "ompi-srcs",
    outs = out_files,
    local = 1,
    cmd = "\n".join([
        'export INSTALL_DIR=$$(pwd)/$(@D)',
        'export TMP_DIR=$$(mktemp -d -t ompi.XXXXX)',
        'mkdir -p $$TMP_DIR',
        'cp -pLR $$(pwd)/external/ompi3/* $$TMP_DIR',
        'cd $$TMP_DIR',
        './configure --prefix=$$INSTALL_DIR --enable-mpi-java --disable-mpi-fortran',
        './configure --prefix=$$INSTALL_DIR --enable-mpi-java --with-jdk-bindir/opt/jdk-11.0.4/bin --with-jdk-headers=/opt/jdk-11.0.4/include',
        'make -j 4; make install',
        'rm -rf $$TMP_DIR',
    ]),
)

filegroup(
    name = "ompi-lib-files",
    srcs = lib_files,
)

filegroup(
    name = "ompi-bin-files",
    srcs = bin_files,
)

filegroup(
    name = "ompi-include-files",
    srcs = include_files,
)

filegroup(
    name = "ompi-etc-files",
    srcs = etc_files,
)

filegroup(
    name = "ompi-jar-files",
    srcs = jar_files,
)

