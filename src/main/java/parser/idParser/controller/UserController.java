package parser.idParser.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import parser.idParser.model.Candidate;
import parser.idParser.service.CandidateRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static parser.idParser.controller.DetectText.parseDataForUser;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private CandidateRepository candidateRepository;
    private static Logger log = LoggerFactory.getLogger(UserController.class);
    public static String uploadDir = System.getProperty("user.dir") + "/uploads/documents/";

    @GetMapping
    public String listCandidates(Model model){
        model.addAttribute("candidateList", candidateRepository.findAll());
        return "candidates/index";
    }

    @GetMapping("candidate/add")
    public String renderCandidateForm(){

        return "candidates/create";
    }


    @GetMapping("candidate/edit/{id}")
    public String renderCandidateEditForm(@PathVariable UUID id, Model model){

        Optional<Candidate> candidateRep = candidateRepository.findById(id);
        if(candidateRep.isPresent()){
            Candidate candidate = candidateRep.get();
            model.addAttribute("candidate", candidate);
        }

        model.addAttribute("id", id);
        return "candidates/create";
    }

    @PostMapping("candidate/add")
    public String addCandidate(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("cnp") long cnp,
            @RequestParam("address") String address,
            @RequestParam(required = false, name="image") String image
            )
    {
        Candidate candidate = new Candidate();
        candidate.setFirstName(firstName);
        candidate.setLastName(lastName);
        candidate.setCnp(cnp);
        candidate.setAddress(address);
        if(image != null){
            candidate.setImage(image);
        }

        candidateRepository.save(candidate);

        return "redirect:/";
    }

    @PostMapping("candidate/edit/{id}")
    public String updateCandidate(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("cnp") long cnp,
            @RequestParam("address") String address,
            @RequestParam(required=false, name="image") String image,
            @PathVariable UUID id
    )
    {
        Optional<Candidate> candidateRep = candidateRepository.findById(id);
        if(candidateRep.isPresent()){
            Candidate candidate = candidateRep.get();
            candidate.setFirstName(firstName);
            candidate.setLastName(lastName);
            candidate.setCnp(cnp);
            candidate.setAddress(address);

            candidateRepository.save(candidate);
        }

        return "redirect:/";
    }

    @GetMapping("candidate/doc/add")
    public String renderDocumentForm(){
        return "candidates/document-add";
    }

    @PostMapping("candidate/doc/add")
    public String uploadDocumentForm(@RequestParam("document") MultipartFile document, Model model){
        String message;
        boolean success;
        String imageUrl;
        try {
            imageUrl = saveDocument(document);
            message = "File successfully uploaded";
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            message = "Could not upload document";
            success = false;

            model.addAttribute("candidateList", candidateRepository.findAll());
            model.addAttribute("message", message);
            model.addAttribute("success", success);

            return "candidates/index";
        }

        String path = Paths.get(uploadDir, imageUrl).toString();
        UserData userData = parseDataForUser(path);

        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("userData", userData);

        DocumentData documentData = userData.getDocumentData();
        Map<String, Integer> wordsWithConfidence = documentData.getWordsWithConfidence();

        //below lines can be used for debugging, to see the data in different ways. words and confidence separated.
//        ArrayList<String> wordsList = documentData.getWordListText();
//        ArrayList<Float> allConfidenceList = documentData.getAllConfidenceList();
//        model.addAttribute("confidenceScore", allConfidenceList);
//        model.addAttribute("data", wordsList);

        model.addAttribute("wordsWithConfidence", wordsWithConfidence);
        model.addAttribute("minConfidence", documentData.getMinConfidence());

        return "candidates/auto-create";
    }

    @GetMapping("candidate/delete/{id}")
    public String deleteCandidate(@PathVariable UUID id){
        candidateRepository.deleteById(id);

        return "redirect:/";
    }

    private String saveDocument(MultipartFile document) throws Exception {
        byte[] bytes = document.getBytes();
        String originalName = document.getOriginalFilename();
        Path path = Paths.get(uploadDir, originalName);
        Files.write(path, bytes);
        return originalName;
    }
}
