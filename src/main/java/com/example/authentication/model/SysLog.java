package com.example.authentication.model;


import com.example.authentication.response.SysLogResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SqlResultSetMapping(
        name = "SysLogResponseMapping",
        classes = @ConstructorResult(
                targetClass = SysLogResponse.class,
                columns = {
                        @ColumnResult(name = "yearMonth", type = String.class),
                        @ColumnResult(name = "count", type = Integer.class)
                }
        )
)
@NamedNativeQuery(
        name = "SysLog.getFilteredSysLog",
        query = "WITH MonthSeries AS ( " +
                "    SELECT FORMAT(CAST(:startDate AS DATE), 'yyyy/MM') AS yearMonth " +
                "    UNION ALL " +
                "    SELECT FORMAT(DATEADD(MONTH, 1, CAST(yearMonth + '/01' AS DATE)), 'yyyy/MM') " +
                "    FROM MonthSeries " +
                "    WHERE DATEADD(MONTH, 1, CAST(yearMonth + '/01' AS DATE)) <= :endDate " +
                ") " +
                "SELECT ms.yearMonth AS yearMonth, " +
                "CASE WHEN COUNT(*) = 1 THEN NULL ELSE COUNT(*) END AS count " +
                "FROM MonthSeries ms " +
                "LEFT JOIN SysLogs s ON FORMAT(s.createdTime, 'yyyy/MM') = ms.yearMonth " +
                "AND s.createdTime >= :startDate " +
                "AND s.createdTime < :endDate " +
                "AND s.method = :method " +
                "GROUP BY ms.yearMonth " +
                "ORDER BY ms.yearMonth " +
                "OPTION (MAXRECURSION 0)",
        resultSetMapping = "SysLogResponseMapping"
)
@Table(name = "SysLogs")
public class SysLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SysLogId")
    private Long sysLogId;

    @Column(name = "CreatedTime", nullable = false)
    private Date createdTime;

    @Column(name = "Ver", nullable = false)
    private int ver;

    @Column(name = "SysLogType", nullable = false)
    private int sysLogType;

    @Column(name = "OnAzure")
    private Boolean onAzure;

    @Column(name = "MachineName")
    private String machineName;

    @Column(name = "ServiceName")
    private String serviceName;

    @Column(name = "TenantName")
    private String tenantName;

    @Column(name = "Application")
    private String application;

    @Column(name = "Class")
    private String sys_class;

    @Column(name = "Method")
    private String method;

    @Column(name = "RequestData", columnDefinition = "nvarchar(max)")
    private String requestData;

    @Column(name = "HttpMethod")
    private String httpMethod;

    @Column(name = "RequestSize")
    private Integer requestSize;

    @Column(name = "ResponseSize")
    private Integer responseSize;

    @Column(name = "Elapsed")
    private Float elapsed;

    @Column(name = "ApplicationAge")
    private Float applicationAge;

    @Column(name = "ApplicationRequestInterval")
    private Float applicationRequestInterval;

    @Column(name = "SessionAge")
    private Float sessionAge;

    @Column(name = "SessionRequestInterval")
    private Float sessionRequestInterval;

    @Column(name = "WorkingSet64")
    private Long workingSet64;

    @Column(name = "VirtualMemorySize64")
    private Long virtualMemorySize64;

    @Column(name = "ProcessId")
    private Integer processId;

    @Column(name = "ProcessName")
    private String processName;

    @Column(name = "BasePriority")
    private Integer basePriority;

    @Column(name = "Url", columnDefinition = "nvarchar(max)")
    private String url;

    @Column(name = "UrlReferer", columnDefinition = "nvarchar(max)")
    private String urlReferer;

    @Column(name = "UserHostName")
    private String userHostName;

    @Column(name = "UserHostAddress")
    private String userHostAddress;

    @Column(name = "UserLanguage")
    private String userLanguage;

    @Column(name = "UserAgent", columnDefinition = "nvarchar(max)")
    private String userAgent;

    @Column(name = "SessionGuid")
    private String sessionGuid;

    @Column(name = "ErrMessage")
    private String errMessage;

    @Column(name = "ErrStackTrace", columnDefinition = "nvarchar(max)")
    private String errStackTrace;

    @Column(name = "InDebug")
    private Boolean inDebug;

    @Column(name = "AssemblyVersion")
    private String assemblyVersion;

    @Column(name = "Comments", columnDefinition = "nvarchar(max)")
    private String comments;

    @Column(name = "Creator", nullable = false)
    private int creator;

    @Column(name = "Updator", nullable = false)
    private int updator;

    @Column(name = "UpdatedTime", nullable = false)
    private Date updatedTime;

    public SysLog(String machineName, String authenticate) {
        this.machineName = machineName;
        this.method = authenticate;

    }
}