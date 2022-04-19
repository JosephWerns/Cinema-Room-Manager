package cinema

const val ROOM_SPLIT = 2
const val SMALL_VENUE = 60
const val TICKET_PRICE_SMALL_VENUE = 10
const val TICKET_PRICE_LARGE_VENUE_FRONT = 10
const val TICKET_PRICE_LARGE_VENUE_BACK = 8

fun main() {

    println("Enter the number of rows:")
    val rows = readLine()!!.toInt()
    println("Enter the number of seats in each row:")
    val seatsPerRow = readLine()!!.toInt()

    // initialize seating
    val cinemaSeats = MutableList(rows) {MutableList(seatsPerRow) {'S'} }
    var choice = "1"

    while (choice != "0") {
        println()
        println("1. Show the seats")
        println("2. Buy a ticket")
        println("3. Statistics")
        println("0. Exit")
        choice = readLine()!!

        when (choice) {
            "1" -> printSeats(cinemaSeats)
            "2" -> buyTicket(cinemaSeats)
            "3" -> printStatistics(cinemaSeats)
        }
    }
}

fun printSeats(seats: MutableList<MutableList<Char>>) {
    val rows = seats.size
    val seatsRow = seats[0].size
    println()
    println("Cinema:")
    // print header
    print("  ")
    for (i in 1..seatsRow) {
        print("$i ")
    }
    println()
    for (i in 1..rows) {
        print("$i ")
        for (j in 1..seatsRow) {
            print(seats[i - 1][j - 1] + " ")
        }
        println()
    }
}

fun buyTicket(seats: MutableList<MutableList<Char>>) {
    val (rows, seatsRow) = rowsAndColumns(seats)

    var ticketPurchaseFlag = false
    var selectedRow = 0
    var selectedSeat = 0

    while (!ticketPurchaseFlag) {
        println()
        // Buyer selects seat
        println("Enter a row number:")
        selectedRow = readLine()!!.toInt()
        println("Enter a seat number in that row:")
        selectedSeat = readLine()!!.toInt()

        ticketPurchaseFlag = if (selectedRow > rows  || selectedSeat > seatsRow) {
            println("\nWrong input!")
            false
        } else if (seats[selectedRow - 1][selectedSeat-1] == 'B')  {
            println("\nThat ticket has already been purchased!")
            false
        } else {
            true
        }
    }

    // Seat is marked as bought
    seats[selectedRow - 1][selectedSeat - 1] = 'B'

    // print price of seat
    val ticketPrice = ticketPrice(rows, seatsRow, selectedRow)
    println("\nTicket price: $$ticketPrice")
}

fun ticketPrice(rows: Int, seatsPerRow: Int, selectedRow: Int): Int {
    val totSeats = rows * seatsPerRow
    if (totSeats <= SMALL_VENUE ) {
        return TICKET_PRICE_SMALL_VENUE
    }
    val mid = rows / ROOM_SPLIT
    return if (selectedRow <= mid) {
        TICKET_PRICE_LARGE_VENUE_FRONT
    }
    else {
        TICKET_PRICE_LARGE_VENUE_BACK
    }
}

fun printStatistics(seats: MutableList<MutableList<Char>>) {

    // Compute the number of total seats by getting rows and columns
    val (rows, seatsRow) = rowsAndColumns(seats)
    val totalSeats = rows * seatsRow

    // Compute event revenue, full house revenue and number of seats sold
    var fullHouseRevenue = 0
    var eventRevenue = 0
    var seatsSold = 0

    for (i in 0 until rows) {
        for(j in 0 until seatsRow ) {
            val tkPrice = ticketPrice(rows, seatsRow, i + 1)

            // calculate full house revenue
            fullHouseRevenue += tkPrice

            // if seat is sold, calculate number of seats sold and event revenue
            if (seats[i][j] == 'B') {
                seatsSold += 1
                eventRevenue += tkPrice
            }
        }
    }

    println("\nNumber of purchased tickets: $seatsSold")

    // Compute percentage of sold seats to 2 percentage points
    val percentage = seatsSold / totalSeats.toDouble() * 100.0
    val formatPercentage = "%.2f".format(percentage)

    println("Percentage: $formatPercentage%")

    // Compute revenue for seats sold

    println("Current income: $$eventRevenue")

    // Compute total income if all seats were sold
    println("Total income: $$fullHouseRevenue")
}

fun rowsAndColumns(seats: MutableList<MutableList<Char>>): Pair<Int, Int> {
    val rows = seats.size
    val seatsRow = seats[0].size

    return Pair(rows, seatsRow)
}
