$userPath = [Environment]::GetEnvironmentVariable('PATH', 'User')
$mavenBin = 'D:\Aahara\tools\apache-maven-3.9.9\bin'
if ($userPath -notmatch [regex]::Escape($mavenBin)) {
    if ($userPath -and -not $userPath.EndsWith(';')) {
        $userPath += ';'
    }
    $userPath += $mavenBin
    [Environment]::SetEnvironmentVariable('PATH', $userPath, 'User')
    Write-Host "Maven added to User PATH."
} else {
    Write-Host "Maven already in User PATH."
}

$javaHome = 'C:\Users\gowth.jdks\ms-21.0.6'
[Environment]::SetEnvironmentVariable('JAVA_HOME', $javaHome, 'User')
Write-Host "JAVA_HOME set to $javaHome."
