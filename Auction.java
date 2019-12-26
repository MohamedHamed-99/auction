import java.util.*;
/**
 * A simple model of an auction.
 * The auction maintains a list of lots of arbitrary length.
 *
 * @author David J. Barnes and Michael Kolling.
 * @version 2006.03.30
 *
 * @author (of AuctionSkeleton) Lynn Marshall
 * @version 2.0
 * 
 * @author Mohamed Hamed    
 * @version 2019.02.02
 * 
 */
public class Auction
{
    /** The list of Lots in this auction. */
    private ArrayList<Lot> lots;
    /** 
     * The number that will be given to the next lot entered
     * into this auction.  Every lot gets a new number, even if some lots have
     * been removed.  (For example, if lot number 3 has been deleted, we don't
     * reuse it.)
     */
    private int nextLotNumber;
    private boolean isopen;
    /**
     * Create a new auction.
     */
    public Auction()
    {
        lots = new ArrayList<Lot>();
        nextLotNumber = 1;
        isopen = true;
    }

    /**
     * Add a second constructor here.  This constructor takes
     * an Auction as a parameter.  Provided the auction parameter
     * is closed, the constructor creates a new auction containing
     * the unsold lots of the closed auction.  If the auction parameter
     * is still open or null, this constructor behaves like the
     * default constructor.
     * 
     * @param oldauction the old acution that still contains unsold lots
     */
    public Auction(Auction oldAuction)
    {
        if (!oldAuction.isopen){
            this.lots = oldAuction.getNoBids();
            nextLotNumber = oldAuction.nextLotNumber;
        }else{
            lots = new ArrayList<Lot>();
            nextLotNumber = 1;
        }
        isopen = true;
    }

    /**
     * Enter a new lot into the auction.  Returns false if the
     * auction is not open or if the description is null.
     *
     * @param description A description of the lot.
     * 
     * @return true if true is the auction is open false otherwise
     */
    public boolean enterLot(String description)
    {
        if(!isopen || description == null){
            return false;
        }else{
            lots.add(new Lot(nextLotNumber, description));
            nextLotNumber++;
            return true;
        }
    }

    /**
     * Show the full list of lots in this auction.
     *
     * Print a blank line first, to make our output look nicer. 
     * If there are no lots, a message printed indicating this. 
     */
    public void showLots()
    {
        System.out.println();
        if(lots.isEmpty()){
            System.out.println("No lots had been entered yet");
        }else{
            for(Lot lot : lots) {
                System.out.println(lot.toString());
            }
        }
    }

    /**
     * Bid for a lot.
     * Prints a message indicating whether the bid is successful or not.
     * First print a blank line.  
     * Then print whether or not the bid is successful.
     * If the bid is successful, also print the
     * lot number, high bidder's name, and the bid value.
     * If the bid is not successful, also print the lot number 
     * and high bid (but not the high bidder's name).
     * 
     * Returns false if the auction is closed, the lot doesn't
     * exist, the bidder is null, or the bid was not positive
     * and true otherwise (even if the bid was not high enough).
     *
     * @param number The lot number being bid for.
     * @param bidder The person bidding for the lot.
     * @param value  The value of the bid.
     * 
     * @return true if the bid is valid false otherwise
     */
    public boolean bidFor(int lotNumber, Person bidder, long value)
    {
        Lot selectedLot = getLot(lotNumber);
        System.out.println();
        if(selectedLot == null || !isopen || value < 0 || bidder == null) {
            return false;
        }
        Bid bid = new Bid(bidder, value);
        boolean successful = selectedLot.bidFor(bid);
        if(successful) {
            System.out.println("The bid for lot number " +
                lotNumber + " was successful.");
            System.out.println("Lot number: " + lotNumber + ", HighestBidder: " + selectedLot.getHighestBid().getBidder().getName() + 
                ", BidValue: " + selectedLot.getHighestBid().getValue());
        }else {
            Bid highestBid = selectedLot.getHighestBid();
            System.out.println("Lot number: " + lotNumber +" already has a bid of: " +
                highestBid.getValue());
        }
        return true;
    }

    /**
     * Return the lot with the given number. 
     * Do not assume that the lots are stored in numerical order.   
     *   
     * Returns null if the lot does not exist.
     *
     * @param lotNumber The number of the lot to return.
     *
     * @return the Lot with the given number
     */
    public Lot getLot(int lotNumber)
    {
        System.out.println();
        if((lotNumber >= 1) && (lotNumber < nextLotNumber)) {
            Lot selectedLot = null;
            for(Lot lot : lots){
                if(lot.getNumber() == lotNumber){
                    selectedLot = lot;
                }
            }
            if (selectedLot == null){
                System.out.println("Lot number: " + lotNumber +
                    " does not exist.");
            }
            return selectedLot;
        }
        System.out.println("Lot number: " + lotNumber +" is invalid");
        return null;
    } 

    /**
     * Closes the auction and prints information on the lots.
     * First print a blank line.  Then for each lot,
     * its number and description are printed.
     * If it did sell, the high bidder and bid value are also printed.  
     * If it didn't sell, print that it didn't sell.
     *
     * @return false if the auction is already closed, true otherwise.
     */
    public boolean close()
    {
        System.out.println();
        if(!isopen){
            return false;
        }
        for(Lot lot : lots) {
            if(lot.getHighestBid() != null){
                System.out.println(lot.getDescription() + " sold for " +
                    lot.getHighestBid().getValue() + "$" + " to "
                    + lot.getHighestBid().getBidder().getName() );
            }else{
                System.out.println(lot.getDescription() + " has not been sold");
            }
        }
        isopen = false;
        return true;   
    }

    /**
     * Returns an ArrayList containing all the items that have no bids so far.
     * (or have not sold if the auction has ended).
     * 
     * @return an ArrayList of the Lots which currently have no bids
     */
    public ArrayList<Lot> getNoBids()
    {
        ArrayList<Lot> remaininglots = new ArrayList<Lot>();
        for(Lot lot : lots){
            if(lot.getHighestBid() == null){
                remaininglots.add(lot);
            }
        }
        return remaininglots; 
    }

    /**
     * Remove the lot with the given lot number, as long as the lot has
     * no bids, and the auction is open.  
     * You must use an Iterator object to search the list and,
     * if applicable, remove the item.
     * Do not assume that the lots are stored in numerical order.
     *
     * Returns true if successful, false otherwise (auction closed,
     * lot does not exist, or lot has a bid).
     *
     * @param number The number of the lot to be removed.
     * 
     * @return returns true if the method did remove false otherwise
     */
    public boolean removeLot(int number)
    {
        if((number >= 1) && (number < nextLotNumber) && isopen){
            Iterator<Lot> iterator = lots.iterator();
            while(iterator.hasNext()){
                Lot nextlot = iterator.next();
                if(nextlot.getNumber() == number && nextlot.getHighestBid() == null ){
                    iterator.remove();
                    return true;
                }
            }
        }
        return false;
    }
}