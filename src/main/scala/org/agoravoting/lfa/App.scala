package org.agoravoting.lfa

/* import com.tinkerpop.furnace.generators._
import com.tinkerpop.blueprints.Graph
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter
import com.tinkerpop.blueprints.Vertex*/

import java.io._
import org.jgrapht._
import org.jgrapht.generate._
import org.jgrapht.graph._
import org.jgrapht.traverse._
import org.jgrapht.ext._
import org.jgrapht.alg._

import collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer
import scala.annotation.tailrec

object App extends Application {           
    val edgeVertexRatio = 3
    val vertices = 100
    val questions = 30
    
    ratioTrend(1.0, 10.0, vertices, questions)    
    
    /* 
    val graph = getRandomGraph(vertices, edgeVertexRatio)            
    val lfa = new Lfa2(questions, graph)                       
    val begin = lfa.randomState(4)
    begin.print
    println("\n\n\n")
    val end1 = lfa.search(begin, 3000, 1)
    val end2 = lfa.searchSA(begin, 3000, 1)
    // val end2 = lfa.search(begin, 5000, 10, true)
    // val end2 = lfa.searchSA(begin, 5000, 20)
    
    println
    println(lfa.score(end1) + " " + lfa.score(begin) + "(" + (questions * vertices * 0.5) + ") " + " " + lfa.scoreRaw(end1) + lfa.scoreRaw(begin) +  " (" + (questions * vertices) +  ")")
    println(lfa.score(end2) + " " + lfa.score(begin) + "(" + (questions * vertices * 0.5) + ") " + " " + lfa.scoreRaw(end2) + lfa.scoreRaw(begin) +  " (" + (questions * vertices) + ")")
    
    exportGraph(graph)
    */
    
    
              
    // println(end.sourcesForQuestion(0))
    
    // exportMap(begin.getAssignments, "id,begin", "begin")
    // exportMap(end.getAssignments, "id,end", "end")
    
    def ratioTrend(ratioMin: Double, ratioMax: Double, vertices: Int, questions: Int, inc: Double = 0.5) = {
        val ratios = ratioMin until (ratioMax, inc)        
        val values = ratios.map{ r =>             
            val graph = getRandomGraph(vertices, r)
            val lfa = new Lfa2(questions, graph)
            println(lfa.maxScore)
            val state = lfa.randomState(5)
            val end = lfa.search(state, 3000, 1)
            List(r, lfa.score(state), lfa.score(end))
        }
        
        exportList(values, "ratio-score")
    }
    
    def getRandomGraph(vertices: Int, edgeVertexRatio: Double) = {        
        val graph = new SimpleDirectedGraph[Object, DefaultEdge](classOf[DefaultEdge])        
        val generator = new RandomGraphGenerator[Object, DefaultEdge](vertices, (vertices * edgeVertexRatio).toInt)
        val vFactory = new ClassBasedVertexFactory(classOf[Object])
        generator.generateGraph(graph, vFactory, null)    
        
        graph
    }
    
    def getScaleFreeGraph(vertices: Int) = {
        val graph = new SimpleDirectedGraph[Object, DefaultEdge](classOf[DefaultEdge])        
        val generator = new ScaleFreeGraphGenerator[Object, DefaultEdge](vertices)                    
        val vFactory = new ClassBasedVertexFactory(classOf[Object])
        generator.generateGraph(graph, vFactory, null)
        
        graph
    }
           
    def exportGraph(graph: Graph[Object, DefaultEdge], lfa: Lfa2) = {
        val exporter = new GraphMLExporter[Object, DefaultEdge](new LfaIdProvider(lfa.vmap), null, new IntegerEdgeNameProvider, null)
        exporter.export(new FileWriter("graph.graphml"), graph)
    }

    def exportList[T](lines: Seq[Seq[T]], fileName: String, header: String = "") = {
        val pw = new java.io.PrintWriter(fileName + ".csv" , "UTF-8")
        // pw.write("id," + fileName + "\n")
        if(header.length > 0) pw.write(header + "\n")
        lines.foreach( lines => pw.write(lines.mkString(",") + "\n"))
        pw.close
    }
}

trait Pause {
    def pause(obj: Any) = {
        println("* " + obj.toString + "*\n(press 'q' to quit, any key to continue)")
        if(System.in.read == 113) System.exit(0)
    }
}

class LfaIdProvider(val vmap: Map[Object, Int]) extends VertexNameProvider[Object] {
    def getVertexName(vertex: Object) = {
        vmap(vertex).toString
    }
}

/* val graph = new TinkerGraph
    for(i <- 1 to 50) {
        graph.addVertex(null)
    }
    
    val generator = new DistributionGenerator("test")
    
    val d = new PowerLawDistribution(2.1)
    
    generator.setOutDistribution(d)
    generator.generate(graph, 500)
    
    // println(graph.getEdges)
    GraphMLWriter.outputGraph(graph, new FileOutputStream("test.graphml"))*/