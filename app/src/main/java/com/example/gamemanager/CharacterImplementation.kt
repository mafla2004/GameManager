package com.example.gamemanager

class CharacterImplementation(
    private val ownerProject:   Project,
    private var name:           String,
    private var species:        String,
    private var birth:          String,     // May not be a standard date, e.g.: "Imperial period, 5th year"
    private var age:            String      // May not be a simple integer, e.g.: "Unknown" or "40 millennia" or "Older than you"
): Character
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
    public override fun getOwnerProject():               Project         = ownerProject
    public override fun getName():                       String          = name
    public override fun getAliases():                    String          = aliases
    public override fun getSpecies():                    String          = species
    public override fun getBirth():                      String          = birth
    public override fun getAge():                        String          = age
    public override fun getAspect():                     String          = aspect
    public override fun getPersonality():                String          = personality
    public override fun getBackstory():                  List<String>    = backstory.toList()
    public override fun getBStoryParagraph(index: Int):  String          = backstory[index]
    public override fun getBStoryParagraphCount():       Int             = backstory.size

    // SETTERS AND MODIFIER METHODS
    public override fun setName(newName: String)                     { name = newName }
    public override fun setAliases(newAliases: String)               { aliases = newAliases }
    public override fun setSpecies(newSpecies: String)               { species = newSpecies }
    public override fun setBirth(newBirth: String)                   { birth = newBirth }
    public override fun setAge(newAge: String)                       { age = newAge}
    public override fun setAspect(newAspect: String)                 { aspect = newAspect }
    public override fun setPersonality(newPers: String)              { personality = newPers }
    public override fun setBStoryParagraph(index: Int, par: String)  { backstory[index] = par }
    public override fun addBStoryParagraph(par: String)              { backstory += par }
    public override fun removeBStoryParagraph(index: Int)            { backstory.removeAt(index) }
}