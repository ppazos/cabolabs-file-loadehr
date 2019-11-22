package com.cabolabs.file_loadehr

import com.cabolabs.ehrserver.*

class Main {

   static void main(String[] args)
   {

      if (args.size() == 0)
      {
         println "missing path as args[0]"
         System.exit(-1)
      }

      def path = args[0]

      def folder = new File(path)

      if (!folder.exists())
      {
         println "path ${path} doesn't exists"
      }

      //println folder.size()

      /*
      folder.eachFileMatch(~/.*\.xml/) { commit ->
         println commit.absolutePath
      }
      */

      def ehrserver = new EhrServerAsyncClient('http://', 'localhost', 8090, '/ehr')
      def res = ehrserver.login('orgman', 'orgman', '123456')
      if (res.status in 200..299)
      {}
      else
      {
         println "ERROR: "+ res.message
         System.exit(-1)
      }

      def ehrs = ehrserver.getEhrs(1, 0)
      println ehrs

      if (ehrs.result.ehrs.size() == 0)
      {
         println "There are no EHRs in the server"
         System.exit(-1)
      }
      def ehr_uid = ehrs.result.ehrs[0].uid


      //def count = 50 // limit the number of commits
      folder.eachFileMatch(~/.*\.xml/) { commit ->

         //println commit.absolutePath

         res = ehrserver.commit(ehr_uid, commit.text, 'Pablo', 'CABOLABS-LOADEHR')
         if (res.status in 200..299)
         {}
         else
         {
            println "ERROR: "+ res.message
            System.exit(-1)
         }

         //count--
         //if (count == 0) System.exit(0)
      }
   }
}
