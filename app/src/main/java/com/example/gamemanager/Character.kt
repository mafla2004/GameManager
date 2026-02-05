package com.example.gamemanager

open class Character(
    private val ownerProject:   Project,
    private var name:           String,
    private var species:        String,
    private var birth:          String,     // May not be a standard date, e.g.: "Imperial period, 5th year"
    private var age:            String      // May not be a simple integer, e.g.: "Unknown" or "40 millennia" or "Older than you"
)
{

    init
    {

    }

    private var aspect: String = ""
    private var personality: String = ""
    private var backstory: MutableList<String> = mutableListOf()            // Character backstories are usually more complex than other traits,
    // I have debated whether using a list of strings (for multiple paragraphs)
    // or a single huge string, eventually I settled for a list of stringe because,
    // if the user wants to edit a specific paragraph or add one / remove one, we don't have to
    // reinitialize the entire backstory, just the paragraph we need.
    // It's also easier to handle it programmatically.
    private var aliases: String = ""                                        // Aliases of the character, stored as a string because there's no gain from
    // using a collection

    // GETTERS
    public fun getOwnerProject():               Project         = ownerProject
    public fun getName():                       String          = name
    public fun getAliases():                    String          = aliases
    public fun getSpecies():                    String          = species
    public fun getBirth():                      String          = birth
    public fun getAge():                        String          = age
    public fun getAspect():                     String          = aspect
    public fun getPersonality():                String          = personality
    public fun getBackstory():                  List<String>    = backstory.toList()
    public fun getBStoryParagraph(index: Int):  String          = backstory[index]
    public fun getBStoryParagraphCount():       Int             = backstory.size

    // SETTERS AND MODIFIER METHODS
    public fun setName(newName: String)                     { name = newName }
    public fun setAliases(newAliases: String)               { aliases = newAliases }
    public fun setSpecies(newSpecies: String)               { species = newSpecies }
    public fun setBirth(newBirth: String)                   { birth = newBirth }
    public fun setAge(newAge: String)                       { age = newAge}
    public fun setAspect(newAspect: String)                 { aspect = newAspect }
    public fun setPersonality(newPers: String)              { personality = newPers }
    public fun setBStoryParagraph(index: Int, par: String)  { backstory[index] = par }
    public fun addBStoryParagraph(par: String)              { backstory += par }
    public fun removeBStoryParagraph(index: Int)            { backstory.removeAt(index) }
}