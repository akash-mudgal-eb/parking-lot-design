# Smart Parking Lot - Floor Maintenance Demo (PowerShell)
# This script demonstrates the floor maintenance capabilities

Write-Host "🔧 Smart Parking Lot - Floor Maintenance Demo" -ForegroundColor Green
Write-Host "=============================================" -ForegroundColor Green

$BaseUrl = "http://localhost:8080/api"

function Invoke-ApiRequest {
    param($Description, $Method, $Body, $Endpoint)
    
    Write-Host "`n📡 $Description" -ForegroundColor Cyan
    Write-Host "Request: $Method $BaseUrl$Endpoint" -ForegroundColor Yellow
    
    try {
        if ($Body) {
            Write-Host "Body: $Body" -ForegroundColor Yellow
            $response = Invoke-RestMethod -Uri "$BaseUrl$Endpoint" -Method $Method -Body $Body -ContentType "application/json"
        } else {
            $response = Invoke-RestMethod -Uri "$BaseUrl$Endpoint" -Method $Method
        }
        $response | ConvertTo-Json -Depth 10 | Write-Host
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
        if ($_.Exception.Response) {
            $_.Exception.Response.Content | Write-Host
        }
    }
}

# Check if server is running
Write-Host "🔍 Checking if server is running..."
try {
    Invoke-RestMethod -Uri "$BaseUrl/parking/health" -Method GET | Out-Null
    Write-Host "✅ Server is running!" -ForegroundColor Green
} catch {
    Write-Host "❌ Server is not running. Please start the application first:" -ForegroundColor Red
    Write-Host "   mvn spring-boot:run" -ForegroundColor Yellow
    exit 1
}

Write-Host "`n🔧 FLOOR MAINTENANCE DEMO" -ForegroundColor Magenta
Write-Host "=========================" -ForegroundColor Magenta

# 1. Check initial maintenance status
Invoke-ApiRequest "1. Check Initial Maintenance Status" "GET" $null "/floors/maintenance"

# 2. View all floors before maintenance
Invoke-ApiRequest "2. View All Floors Before Maintenance" "GET" $null "/floors"

# 3. Park some vehicles first
Invoke-ApiRequest "3. Park Vehicle 1 (Should go to any available floor)" "POST" '{"licensePlate":"CAR001","vehicleType":"CAR","ownerName":"Alice"}' "/parking/entry"
Invoke-ApiRequest "4. Park Vehicle 2" "POST" '{"licensePlate":"BIKE001","vehicleType":"MOTORCYCLE","ownerName":"Bob"}' "/parking/entry"

# 4. Check which floors the vehicles are on
Invoke-ApiRequest "5. Check Floor Status After Parking" "GET" $null "/floors"

# 5. Try to set floor 1 to maintenance mode (should fail if vehicles are parked there)
Write-Host "`n⚠️  Attempting to set Floor 1 to maintenance (may fail if occupied)" -ForegroundColor Yellow
Invoke-ApiRequest "6. Try Setting Floor 1 to Maintenance (May Fail)" "PUT" '{"underMaintenance":true,"maintenanceReason":"Regular cleaning and inspection"}' "/floors/1/maintenance"

# 6. Set floor 3 to maintenance mode (likely to succeed)
Invoke-ApiRequest "7. Set Floor 3 to Maintenance Mode" "PUT" '{"underMaintenance":true,"maintenanceReason":"Elevator repair and floor cleaning"}' "/floors/3/maintenance"

# 7. Check maintenance status after setting floor 3
Invoke-ApiRequest "8. Check Maintenance Status After Setting Floor 3" "GET" $null "/floors/maintenance"

# 8. View floor 3 specific status
Invoke-ApiRequest "9. Check Floor 3 Status (Should show 0 available spots)" "GET" $null "/floors/3"

# 9. Try to park another vehicle (should NOT go to floor 3)
Invoke-ApiRequest "10. Park Another Vehicle (Should Avoid Floor 3)" "POST" '{"licensePlate":"CAR002","vehicleType":"CAR","ownerName":"Charlie"}' "/parking/entry"

# 10. Check all floors status
Invoke-ApiRequest "11. Check All Floors After Maintenance and New Parking" "GET" $null "/floors"

# 11. Exit vehicles to clear floor 1
Invoke-ApiRequest "12. Exit Vehicle 1" "POST" '{"licensePlate":"CAR001"}' "/parking/exit"
Invoke-ApiRequest "13. Exit Vehicle 2" "POST" '{"licensePlate":"BIKE001"}' "/parking/exit"

# 12. Now try to set floor 1 to maintenance (should succeed)
Invoke-ApiRequest "14. Set Floor 1 to Maintenance (Should Succeed Now)" "PUT" '{"underMaintenance":true,"maintenanceReason":"Deep cleaning and repainting"}' "/floors/1/maintenance"

# 13. Check maintenance status with two floors
Invoke-ApiRequest "15. Check Maintenance Status (Should Show 2 Floors)" "GET" $null "/floors/maintenance"

# 14. Try to park a vehicle (should only go to floor 2)
Invoke-ApiRequest "16. Park Vehicle (Should Only Go to Floor 2)" "POST" '{"licensePlate":"BUS001","vehicleType":"BUS","ownerName":"Dave"}' "/parking/entry"

# 15. Check overall parking status
Invoke-ApiRequest "17. Check Overall Parking Status" "GET" $null "/parking/status"

# 16. Disable maintenance mode for floor 3
Invoke-ApiRequest "18. Disable Maintenance Mode for Floor 3" "PUT" '{"underMaintenance":false}' "/floors/3/maintenance"

# 17. Check final maintenance status
Invoke-ApiRequest "19. Final Maintenance Status" "GET" $null "/floors/maintenance"

# 18. Park another vehicle (should now be able to use floor 3 again)
Invoke-ApiRequest "20. Park Vehicle (Can Now Use Floor 3 Again)" "POST" '{"licensePlate":"CAR003","vehicleType":"CAR","ownerName":"Eve"}' "/parking/entry"

# 19. Final status check
Invoke-ApiRequest "21. Final All Floors Status" "GET" $null "/floors"

Write-Host "`n🎉 Floor Maintenance Demo completed!" -ForegroundColor Green
Write-Host "====================================" -ForegroundColor Green
Write-Host "Summary of maintenance features demonstrated:" -ForegroundColor White
Write-Host "✅ Set floors to maintenance mode with reasons" -ForegroundColor Green
Write-Host "✅ Prevent vehicle parking on maintenance floors" -ForegroundColor Green
Write-Host "✅ Validation to prevent maintenance when floors are occupied" -ForegroundColor Green
Write-Host "✅ Track which floors are under maintenance" -ForegroundColor Green
Write-Host "✅ Show 0 available spots for maintenance floors" -ForegroundColor Green
Write-Host "✅ Disable maintenance mode to restore normal operations" -ForegroundColor Green
Write-Host "✅ Real-time updates of floor availability" -ForegroundColor Green
