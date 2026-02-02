package com.example.gamemanager

interface Character/*: Serializeable()*/
{

    // GETTERS
    public fun getOwnerProject():               Project
    public fun getName():                       String
    public fun getAliases():                    String
    public fun getSpecies():                    String
    public fun getBirth():                      String
    public fun getAge():                        String
    public fun getAspect():                     String
    public fun getPersonality():                String
    public fun getBackstory():                  List<String>
    public fun getBStoryParagraph(index: Int):  String
    public fun getBStoryParagraphCount():       Int

    // SETTERS AND MODIFIER METHODS
    public fun setName(newName: String)
    public fun setAliases(newAliases: String)
    public fun setSpecies(newSpecies: String)
    public fun setBirth(newBirth: String)
    public fun setAge(newAge: String)
    public fun setAspect(newAspect: String)
    public fun setPersonality(newPers: String)
    public fun setBStoryParagraph(index: Int, par: String)
    public fun addBStoryParagraph(par: String)
    public fun removeBStoryParagraph(index: Int)
}