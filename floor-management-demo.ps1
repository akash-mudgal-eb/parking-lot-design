# Smart Parking Lot - Floor Management Demo (PowerShell)
# This script demonstrates the multi-floor management capabilities

Write-Host "🏢 Smart Parking Lot - Floor Management Demo" -ForegroundColor Green
Write-Host "=============================================" -ForegroundColor Green

$BaseUrl = "http://localhost:8080/api"

function Make-Request {
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

Write-Host "`n🏢 FLOOR MANAGEMENT DEMO" -ForegroundColor Magenta
Write-Host "========================" -ForegroundColor Magenta

# 1. Check initial floors
Make-Request "1. View All Existing Floors" "GET" $null "/floors"

# 2. Get specific floor details
Make-Request "2. Get Floor 1 Details" "GET" $null "/floors/1"
Make-Request "3. Get Floor 2 Details" "GET" $null "/floors/2"
Make-Request "4. Get Floor 3 Details" "GET" $null "/floors/3"

# 3. Add a new floor
Make-Request "4. Add New Floor 4 (5 motorcycle, 8 compact, 4 large)" "POST" '{"floorNumber":4,"motorcycleSpots":5,"compactSpots":8,"largeSpots":4}' "/floors"

# 4. Add individual parking spots
Make-Request "5. Add Individual Compact Spot to Floor 1" "POST" '{"floorNumber":1,"spotType":"COMPACT"}' "/floors/spots"
Make-Request "6. Add Individual Large Spot to Floor 2" "POST" '{"floorNumber":2,"spotType":"LARGE"}' "/floors/spots"

# 5. View updated floors
Make-Request "7. View All Floors After Adding New Floor and Spots" "GET" $null "/floors"

# 6. Get updated floor 4 details
Make-Request "8. Get New Floor 4 Details" "GET" $null "/floors/4"

Write-Host "`n🚗 PARKING WITH FLOOR PREFERENCE DEMO" -ForegroundColor Magenta
Write-Host "=====================================" -ForegroundColor Magenta

# 7. Park some vehicles to see floor allocation
Make-Request "9. Park Motorcycle on Any Floor" "POST" '{"licensePlate":"BIKE001","vehicleType":"MOTORCYCLE","ownerName":"Alice"}' "/parking/entry"
Make-Request "10. Park Car on Any Floor" "POST" '{"licensePlate":"CAR001","vehicleType":"CAR","ownerName":"Bob"}' "/parking/entry"
Make-Request "11. Park Bus on Any Floor" "POST" '{"licensePlate":"BUS001","vehicleType":"BUS","ownerName":"Charlie"}' "/parking/entry"

# 8. Check floor status after parking
Make-Request "12. Check All Floors Status After Parking" "GET" $null "/floors"

# 9. Check overall parking lot status
Make-Request "13. Check Overall Parking Lot Status" "GET" $null "/parking/status"

Write-Host "`n🧹 CLEANUP DEMO" -ForegroundColor Magenta
Write-Host "===============" -ForegroundColor Magenta

# 10. Try to remove an occupied spot (should fail)
Make-Request "14. Try to Remove Occupied Spot (Should Fail)" "DELETE" $null "/floors/spots/1-C-01"

# 11. Exit vehicles first
Make-Request "15. Exit Motorcycle" "POST" '{"licensePlate":"BIKE001"}' "/parking/exit"
Make-Request "16. Exit Car" "POST" '{"licensePlate":"CAR001"}' "/parking/exit"
Make-Request "17. Exit Bus" "POST" '{"licensePlate":"BUS001"}' "/parking/exit"

# 12. Now try to remove a spot (should work)
Make-Request "18. Remove a Parking Spot After Exit" "DELETE" $null "/floors/spots/4-C-01"

# 13. Final status
Make-Request "19. Final Floors Status" "GET" $null "/floors"

Write-Host "`n🎉 Floor Management Demo completed!" -ForegroundColor Green
Write-Host "===================================" -ForegroundColor Green
Write-Host "Summary of floor management features demonstrated:" -ForegroundColor White
Write-Host "✅ View all floors and individual floor details" -ForegroundColor Green
Write-Host "✅ Add new floors with custom spot configuration" -ForegroundColor Green
Write-Host "✅ Add individual parking spots to existing floors" -ForegroundColor Green
Write-Host "✅ Intelligent spot allocation across multiple floors" -ForegroundColor Green
Write-Host "✅ Floor-wise availability tracking" -ForegroundColor Green
Write-Host "✅ Spot removal with occupancy validation" -ForegroundColor Green
Write-Host "✅ Real-time floor status updates" -ForegroundColor Green
