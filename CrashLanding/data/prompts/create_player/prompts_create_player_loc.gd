[
  {
    "key": "create_player_loc",
    "text": "Where were you born?",
    "inputs": [
      {
        "text": "Born in the capital",
        "events": [
          {
            "add_traits": [
              {
                "key": "STARTING_LOC",
                "value": 4
              }
            ]
          }
        ],
        "prompt": "choices_skills_create_player"
      },
      {
        "text": "Born in the sprawling underground.",
        "events": [
          {
            "add_traits": [
              {
                "key": "STARTING_LOC",
                "value": 2
              }
            ]
          }
        ],
        "prompt": "choices_skills_create_player"
      },
      {
        "text": "Born in a frontier colony",
        "events": [
          {
            "add_traits": [
              {
                "key": "STARTING_LOC",
                "value": 3
              }
            ]
          }
        ],
        "prompt": "choices_skills_create_player"
      },
      {
        "text": "Born on a military outpost",
        "events": [
          {
            "add_traits": [
              {
                "key": "WEALTH",
                "value": 2
              },
              {
                "key": "STARTING_LOC",
                "value": 1
              }
            ]
          }
        ],
        "prompt": "choices_skills_create_player"
      }
    ]
  },
  {
    "key": "choices_create_player",
    "text": "What happened in your <0#AGE:timeframe>?",
    "pre_prompt": {
      "increment_trait": {
        "key": "AGE",
        "value": 1
      }
    },
    "inputs": [
      {
        "text": "framed by a rival star",
        "requirement": {
          "all": [
            {
              "trait": "STATUS_CELEBRITY",
              "value": 2,
              "comp": ">="
            },
            {
              "trait": "AGE",
              "value": 4,
              "comp": ">="
            }
          ]
        },
        "prompt": "start_wake_up"
      },
      {
        "text": "caught by the authorities",
        "requirement": {
          "all": [
            {
              "trait": "STATUS_GANG",
              "value": 1,
              "comp": ">="
            },
            {
              "trait": "AGE",
              "value": 4,
              "comp": ">="
            }
          ]
        },
        "prompt": "start_wake_up"
      },
      {
        "text": "framed by a rival elite",
        "requirement": {
          "all": [
            {
              "trait": "WEALTH",
              "value": 3,
              "comp": ">="
            },
            {
              "trait": "AGE",
              "value": 4,
              "comp": ">="
            }
          ]
        },
        "prompt": "start_wake_up"
      },
      {
        "text": "caught fabricating documents to get a job",
        "requirement": {
          "all": [
            {
              "trait": "SKILL_DECEIT",
              "value": 1,
              "comp": ">="
            },
            {
              "trait": "AGE",
              "value": 4,
              "comp": ">="
            }
          ]
        },
        "prompt": "start_wake_up"
      },
      {
        "text": "try to become famous",
        "requirement": {
          "all": [
            {
              "trait": "AGE",
              "value": 4,
              "comp": ">="
            },
            {
              "trait": "STATUS_CELEBRITY",
              "comp": "!"
            },
            {
              "trait": "STATUS_GANG",
              "comp": "!"
            },
            {
              "trait": "PERSONALITY_MOTIVATION",
              "value": 2,
              "comp": ">="
            }
          ]
        },
        "events": [
          {
            "add_trait": {
              "key": "STATUS_CELEBRITY",
              "value": 1
            },
            "increment_trait": {
              "key": "PERSONALITY_MOTIVATION",
              "value": 1
            }
          }
        ],
        "prompt": "start_celebrity"
      },
      {
        "text": "parents made you a child star",
        "requirement": {
          "all": [
            {
              "trait": "AGE",
              "min": 2,
              "max": 3
            },
            {
              "trait": "WEALTH",
              "value": 2,
              "comp": ">="
            },
            {
              "trait": "STATUS_CELEBRITY",
              "comp": "!"
            },
            {
              "trait": "STATUS_GANG",
              "comp": "!"
            }
          ]
        },
        "events": [
          {
            "add_trait": {
              "key": "STATUS_CELEBRITY",
              "value": 2
            }
          }
        ],
        "prompt": "start_celebrity"
      },
      {
        "text": "Get a shady job",
        "requirement": {
          "all": [
            {
              "trait": "WEALTH",
              "value": 3,
              "comp": "!"
            },
            {
              "trait": "STATUS_CELEBRITY",
              "comp": "!"
            },
            {
              "trait": "STATUS_EMPLOYED",
              "comp": "!"
            },
            {
              "trait": "STATUS_GANG",
              "comp": "!"
            },
            {
              "trait": "SKILL_EDUCATION",
              "value": 1,
              "comp": "="
            },
            {
              "trait": "AGE",
              "value": 3,
              "comp": ">="
            }
          ]
        },
        "events": [
          {
            "add_trait": {
              "key": "STATUS_EMPLOYED",
              "value": 1
            }
          }
        ],
        "prompt": "start_employment"
      },
      {
        "text": "Enter the corporate world",
        "requirement": {
          "all": [
            {
              "trait": "WEALTH",
              "value": 3,
              "comp": "!"
            },
            {
              "trait": "STATUS_CELEBRITY",
              "comp": "!"
            },
            {
              "trait": "STATUS_EMPLOYED",
              "comp": "!"
            },
            {
              "trait": "STATUS_GANG",
              "comp": "!"
            },
            {
              "trait": "SKILL_EDUCATION",
              "value": 2,
              "comp": ">="
            },
            {
              "trait": "AGE",
              "value": 4,
              "comp": ">="
            }
          ]
        },
        "events": [
          {
            "add_trait": {
              "key": "STATUS_EMPLOYED",
              "value": 2
            },
            "increment_trait": {
              "key": "WEALTH",
              "value": 1,
              "max": 2
            }
          }
        ],
        "prompt": "start_employment"
      },
      {
        "text": "Forced to join the gangs",
        "requirement": {
          "all": [
            {
              "trait": "STARTING_LOC",
              "value": 4,
              "comp": "!"
            },
            {
              "trait": "STATUS_GANG",
              "comp": "!"
            },
            {
              "trait": "STATUS_CELEBRITY",
              "comp": "!"
            },
            {
              "trait": "WEALTH",
              "value": 2,
              "comp": "<="
            }
          ]
        },
        "events": [
          {
            "add_trait": {
              "key": "STATUS_GANG",
              "value": 1
            }
          }
        ],
        "prompt": "start_gang"
      },
      {
        "text": "Continue working in the gangs",
        "requirement": {
          "trait": "STATUS_GANG"
        },
        "prompt": "start_gang"
      },
      {
        "text": "Escape the gangs",
        "requirement": {
          "trait": "STATUS_GANG",
          "value": 3,
          "comp": "<="
        },
        "prompt": "end_gang"
      },
      {
        "text": "Beg on the streets",
        "requirement": {
          "all": [
            {
              "trait": "STARTING_LOC",
              "value": 4,
              "comp": "!"
            },
            {
              "trait": "STATUS_GANG",
              "comp": "!"
            },
            {
              "trait": "STATUS_CELEBRITY",
              "comp": "!"
            },
            {
              "trait": "WEALTH",
              "value": 1,
              "comp": "<="
            }
          ]
        },
        "events": [
          {
            "increment_trait": {
              "key": "SKILL_DECEIT",
              "value": 1
            },
            "decrement_trait": {
              "key": "PERSONALITY_CHARM",
              "value": 1,
              "min": 1
            }
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "Work as a child laborer",
        "requirement": {
          "all": [
            {
              "trait": "STATUS_GANG",
              "comp": "!"
            },
            {
              "trait": "STATUS_CELEBRITY",
              "comp": "!"
            },
            {
              "trait": "WEALTH",
              "value": 2,
              "comp": "<="
            },
            {
              "trait": "STARTING_LOC",
              "values": [
                2,
                3
              ]
            },
            {
              "trait": "AGE",
              "value": 3,
              "comp": "<="
            }
          ]
        },
        "prompt": "start_child_labor"
      },
      {
        "text": "Spend all day getting pampered",
        "requirement": {
          "all": [
            {
              "trait": "STATUS_GANG",
              "comp": "!"
            },
            {
              "trait": "WEALTH",
              "value": 3,
              "comp": ">="
            },
            {
              "trait": "AGE",
              "min": 2,
              "max": 3
            }
          ]
        },
        "events": [
          {
            "decrement_trait": {
              "key": "PERSONALITY_MOTIVATION",
              "value": 1
            }
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "Study in elementary school",
        "requirement": {
          "all": [
            {
              "trait": "STATUS_GANG",
              "comp": "!"
            },
            {
              "trait": "SKILL_EDUCATION",
              "value": 0,
              "comp": "="
            },
            {
              "trait": "WEALTH",
              "value": 2,
              "comp": ">="
            },
            {
              "trait": "AGE",
              "min": 2,
              "max": 3
            }
          ]
        },
        "events": [
          {
            "increment_trait": {
              "key": "SKILL_EDUCATION",
              "value": 1
            }
          }
        ],
        "prompt": "start_school"
      },
      {
        "text": "Study in boarding school",
        "requirement": {
          "all": [
            {
              "trait": "STATUS_GANG",
              "comp": "!"
            },
            {
              "trait": "SKILL_EDUCATION",
              "value": 1,
              "comp": "<="
            },
            {
              "trait": "WEALTH",
              "value": 3,
              "comp": ">="
            },
            {
              "trait": "AGE",
              "min": 2,
              "max": 3
            }
          ]
        },
        "events": [
          {
            "increment_trait": {
              "key": "SKILL_EDUCATION",
              "value": 2,
              "max": 2
            }
          }
        ],
        "prompt": "start_school"
      },
      {
        "text": "Go to university",
        "requirement": {
          "all": [
            {
              "trait": "STATUS_GANG",
              "comp": "!"
            },
            {
              "trait": "SKILL_EDUCATION",
              "value": 2,
              "comp": "="
            }
          ]
        },
        "events": [
          {
            "increment_trait": {
              "key": "SKILL_EDUCATION",
              "value": 1
            }
          }
        ],
        "prompt": "start_university"
      },
      {
        "text": "Kidnapped by ransomers",
        "requirement": {
          "all": [
            {
              "trait": "WEALTH",
              "value": 3,
              "comp": ">="
            },
            {
              "trait": "AGE",
              "min": 2,
              "max": 3
            }
          ]
        },
        "events": [
          {
            "increment_trait": {
              "key": "SKILL_TOUGH",
              "value": 1
            }
          }
        ],
        "prompt": "start_kidnap"
      },
      {
        "text": "Run away from home",
        "requirement": {
          "all": [
            {
              "trait": "WEALTH",
              "value": 3,
              "comp": ">="
            },
            {
              "trait": "AGE",
              "min": 2,
              "max": 3
            }
          ]
        },
        "events": [
          {
            "add_traits": [
              {
                "key": "WEALTH",
                "value": 1
              },
              {
                "key": "STARTING_LOC",
                "value": 2
              }
            ],
            "increment_trait": {
              "key": "PERSONALITY_MOTIVATION",
              "value": 1
            }
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "abducted for corporate special interests",
        "requirement": {
          "all": [
            {
              "trait": "WEALTH",
              "value": 1,
              "comp": "<="
            },
            {
              "trait": "AGE",
              "min": 2,
              "max": 3
            },
            {
              "trait": "STATUS_CELEBRITY",
              "comp": "!"
            },
            {
              "trait": "STATUS_EMPLOYED",
              "comp": "!"
            }
          ]
        },
        "events": [
          {
            "remove_trait": {
              "key": "STATUS_GANG"
            }
          }
        ],
        "prompt": "start_abduction"
      }
    ]
  }
]